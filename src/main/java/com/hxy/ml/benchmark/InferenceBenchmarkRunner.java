package com.hxy.ml.benchmark;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;
import org.tensorflow.framework.ConfigProto;
import org.tensorflow.framework.GPUOptions;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class InferenceBenchmarkRunner {

    public enum Device {
        CPU,
        GPU,
        ;

        public static Device fromDevice(String device) {
            if ("cpu".equalsIgnoreCase(device)) {
                return CPU;
            }
            if ("gpu".equalsIgnoreCase(device)) {
                return GPU;
            }
            throw new IllegalArgumentException();
        }
    }

    public static class RunConfig {
        public String localModelPath;
        public String inputPath;
        public Device device;
        public int deviceId;
        public String parallel;
        public int timeInSeconds;

        public RunConfig(String localModelPath,
                         String inputPath,
                         Device device,
                         int deviceId,
                         String parallel,
                         int timeInSeconds
        ) {
            this.localModelPath = localModelPath;
            this.inputPath = inputPath;
            this.device = device;
            this.deviceId = deviceId;
            this.parallel = parallel;
            this.timeInSeconds = timeInSeconds;
        }

        @Override
        public String toString() {
            return "RunConfig{" +
                    "localModelPath='" + localModelPath + '\'' +
                    ", inputPath=" + inputPath +
                    ", device=" + device +
                    ", deviceId=" + deviceId +
                    ", parallel=" + parallel +
                    ", v=" + timeInSeconds +
                    '}';
        }
    }

    public static RunConfig parseRunConfig() {
        String localModelPath = System.getProperty("local.model.path");
        if (localModelPath == null || localModelPath.length() <= 0) {
            throw new NullPointerException();
        }
        String inputPath = System.getProperty("input.path");
        if (inputPath == null || inputPath.length() <= 0) {
            throw new NullPointerException();
        }
        Device device = Device.fromDevice(System.getProperty("device"));
        int deviceId = Device.CPU == device ? 0 : Integer.parseInt("device.id");
        String parallel = System.getProperty("parallel");
        int timeInSeconds = Integer.parseInt(System.getProperty("time.in.seconds"));
        return new RunConfig(localModelPath, inputPath, device, deviceId, parallel, timeInSeconds);
    }

    public static SavedModelBundle loadModelByConfig(RunConfig runConfig) {
        ConfigProto.Builder configBuilder = ConfigProto.newBuilder()
                .setAllowSoftPlacement(true)
                .setLogDevicePlacement(true);
        if (Device.GPU == runConfig.device) {
            GPUOptions gpuOptions = GPUOptions.newBuilder()
                    .setVisibleDeviceList(String.valueOf(runConfig.deviceId))
                    .setPerProcessGpuMemoryFraction(0.85f)
                    .setAllowGrowth(true)
                    .build();
            configBuilder.setGpuOptions(gpuOptions);
        }

        String tags = "serve";
        ConfigProto configProto = configBuilder.build();
        System.out.println("config -> " + configProto);
        return SavedModelBundle
                .loader(runConfig.localModelPath)
                .withTags(tags)
                .withConfigProto(configProto.toByteArray())
                .load();
    }

    private static List<byte[][]> readExamples(RunConfig runConfig) {
        String inputPath = runConfig.inputPath;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputPath))) {
            List<byte[][]> read = (List<byte[][]>) ois.readObject();
            System.out.println("read.size -> " + read.size());
            return read;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        RunConfig runConfig = parseRunConfig();
        System.out.println("run-config -> " + runConfig);

        SavedModelBundle model = loadModelByConfig(runConfig);
        System.out.println("load model");

        Session session = model.session();
        System.out.println("new session");

        List<byte[][]> examples = readExamples(runConfig);
        int size = examples.size();
        int max = 10000;
        Runnable inference = () -> {
            int rand = ThreadLocalRandom.current().nextInt(max);
            byte[][] inputBytes = examples.get(rand % size);
            Tensor<String> inputBatch = Tensors.create(inputBytes);
            String inputOperation = "input_example_tensor:0";
            String outputOperation = "Sigmoid:0";
            Session.Runner runner = session.runner().feed(inputOperation, inputBatch);
            runner.fetch(outputOperation);
            List<Tensor<?>> result = runner.run();
            for (Tensor<?> tensor : result) {
                float[][] scores = tensor.copyTo(new float[inputBytes.length][1]);
                if (rand < 1) {
                    for (float[] score : scores) {
                        System.out.println("score: " + Arrays.toString(score));
                    }
                }
            }
        };

        String parallelString = runConfig.parallel;
        for (String parallelStr : parallelString.split(",")) {
            int parallel = Integer.parseInt(parallelStr);
            System.out.println("parallel " + parallel + " start");
            ExecutorService executors = Executors.newFixedThreadPool(parallel);
            CountDownLatch cdl = new CountDownLatch(parallel);
            for (int i = 0; i < parallel; i++) {
                executors.execute(() -> {
                    long endTime = System.currentTimeMillis() + runConfig.timeInSeconds * 1000L;
                    while (true) {
                        long start = System.currentTimeMillis();
                        inference.run();
                        long end = System.currentTimeMillis();
                        long cost = end - start;
                        statistic(cost);
                        if (end > endTime) {
                            break;
                        }
                    }

                    cdl.countDown();
                });
            }
            cdl.await();
            executors.shutdown();

            System.out.println("parallel " + parallel + " end");
        }
    }

    public static void statistic(long cost) {

    }

}
