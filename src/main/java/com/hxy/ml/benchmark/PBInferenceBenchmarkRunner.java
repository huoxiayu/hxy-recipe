package com.hxy.ml.benchmark;

import org.apache.commons.io.IOUtils;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;
import org.tensorflow.framework.ConfigProto;
import org.tensorflow.framework.GPUOptions;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;

public class PBInferenceBenchmarkRunner {

    private static final String FILE_PREFIX = "request_pb_serialize_";
    private static final int FILES = 30;

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
        public Device device;
        public int parallel;
        public int batchSize;
        public int epoch;

        public RunConfig(String localModelPath,
                         Device device,
                         int parallel,
                         int batchSize,
                         int epoch
        ) {
            this.localModelPath = localModelPath;
            this.device = device;
            this.parallel = parallel;
            this.batchSize = batchSize;
            this.epoch = epoch;
        }

        @Override
        public String toString() {
            return "RunConfig{" +
                    "localModelPath='" + localModelPath + '\'' +
                    ", device=" + device +
                    ", parallel=" + parallel +
                    ", batchSize=" + batchSize +
                    ", epoch=" + epoch +
                    '}';
        }

    }

    public static RunConfig parseRunConfig() {
        String localModelPath = System.getProperty("local.model.path");
        if (localModelPath == null || localModelPath.length() <= 0) {
            throw new NullPointerException();
        }
        Device device = Device.fromDevice(System.getProperty("device"));
        int parallel = Integer.parseInt(System.getProperty("parallel"));
        int batchSize = Integer.parseInt(System.getProperty("batch.size"));
        int epoch = Integer.parseInt(System.getProperty("epoch"));
        return new RunConfig(localModelPath, device, parallel, batchSize, epoch);
    }

    public static SavedModelBundle loadModelByConfig(RunConfig runConfig) {
        ConfigProto.Builder configBuilder = ConfigProto.newBuilder()
                .setAllowSoftPlacement(true)
                .setLogDevicePlacement(true);
        if (Device.GPU == runConfig.device) {
            String gpuIds = "0,1,2,3";
            GPUOptions gpuOptions = GPUOptions.newBuilder()
                    .setVisibleDeviceList(gpuIds)
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

    public static long action(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - start;
    }

    public static long[] action(Runnable runnable, int times) {
        long[] cost = new long[times];
        for (int i = 0; i < times; i++) {
            cost[i] = action(runnable);
        }
        return cost;
    }

    public static void printP99Cost(long[] costs) {
        Arrays.sort(costs);
        int len = costs.length;
        int idx = (int) Math.floor(len * 0.99D);
        System.out.printf("p99 cost %s millis%n", costs[idx]);
    }

    public static byte[] buildExample() {
        try {
            int fileIdx = ThreadLocalRandom.current().nextInt(FILES);
            File file = new File(FILE_PREFIX + fileIdx);
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[][] buildExamples(RunConfig runConfig) {
        int batchSize = runConfig.batchSize;
        byte[][] examples = new byte[batchSize][];
        for (int i = 0; i < batchSize; i++) {
            examples[i] = buildExample();
        }
        return examples;
    }

    public static void main(String[] args) throws Exception {
        RunConfig runConfig = parseRunConfig();
        System.out.println("run-config -> " + runConfig);

        SavedModelBundle model = loadModelByConfig(runConfig);
        System.out.println("load model");

        Session session = model.session();
        System.out.println("new session");

        byte[][] inputBytes = buildExamples(runConfig);
        Runnable inference = () -> {
            Tensor<String> inputBatch = Tensors.create(inputBytes);
            String inputOperation = "input_example_tensor:0";
            String outputOperation = "Sigmoid:0";
            Session.Runner runner = session.runner().feed(inputOperation, inputBatch);
            runner.fetch(outputOperation);
            List<Tensor<?>> result = runner.run();
            for (Tensor<?> tensor : result) {
                float[][] scores = tensor.copyTo(new float[inputBytes.length][1]);
                if (ThreadLocalRandom.current().nextInt(10000) < 1) {
                    for (float[] score : scores) {
                        System.out.println("score: " + Arrays.toString(score));
                    }
                }
            }
        };

        int warmUpTimes = 100;
        System.out.println("warm-up begin");
        action(inference, warmUpTimes);
        System.out.println("warm-up end");

        LongAdder sumCost = new LongAdder();
        LongAdder inferenceCnt = new LongAdder();
        int parallel = runConfig.parallel;
        for (int e = 0; e < runConfig.epoch; e++) {
            CountDownLatch cdl = new CountDownLatch(parallel);
            for (int i = 0; i < parallel; i++) {
                final int fi = i;

                Thread thread_i = new Thread(() -> {
                    int benchmarkTimes = 100;

                    long[] costs = action(inference, benchmarkTimes);
                    inferenceCnt.add((long) benchmarkTimes * runConfig.batchSize);
                    for (long cost : costs) {
                        sumCost.add(cost);
                    }

                    printP99Cost(costs);
                    cdl.countDown();
                }, "inference-thread-" + i);
                thread_i.start();
            }
            cdl.await();
        }

        System.out.printf("inference-request -> %s%n", inferenceCnt);
        System.out.printf("total cost -> %s millis%n", sumCost);
        System.out.printf("avg cost %s millis%n", sumCost.doubleValue() / inferenceCnt.doubleValue());
    }

}
