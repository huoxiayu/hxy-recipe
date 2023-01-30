package com.hxy.ml.tensorflow;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;
import org.tensorflow.example.Example;
import org.tensorflow.example.Feature;
import org.tensorflow.example.Features;
import org.tensorflow.example.Int64List;
import org.tensorflow.framework.ConfigProto;
import org.tensorflow.framework.GPUOptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TFModelLoadStart {

    public static void main(String[] args) throws Exception {
        String modelPath = "/home/work/model/";
        String tags = "serve";
        String gpuIds = "0,1,2,3";
        GPUOptions gpuOptions = GPUOptions.newBuilder()
                .setVisibleDeviceList(gpuIds)
                .setPerProcessGpuMemoryFraction(0.85f)
                .setAllowGrowth(true)
                .build();
        ConfigProto configProto = ConfigProto.newBuilder()
                .setAllowSoftPlacement(true)
                .setLogDevicePlacement(true)
                .setGpuOptions(gpuOptions)
                .build();
        SavedModelBundle model = SavedModelBundle
                .loader(modelPath)
                .withTags(tags)
                .withConfigProto(configProto.toByteArray())
                .load();
        System.out.println("model: " + model);
        Session session = model.session();
        System.out.println("session: " + session);

        int batchSize = 64;
        byte[][] bytesBatch = new byte[batchSize][];
        for (int i = 0; i < batchSize; i++) {
            bytesBatch[i] = buildExample();
        }

        while (true) {
            long start = System.currentTimeMillis();

            Tensor<String> inputBatch = Tensors.create(bytesBatch);
            String inputOperation = "input_example_tensor:0";
            String outputOperation = "Sigmoid:0";
            Session.Runner runner = session.runner().feed(inputOperation, inputBatch);
            runner.fetch(outputOperation);
            List<Tensor<?>> result = runner.run();
            for (Tensor<?> tensor : result) {
                float[][] scores = tensor.copyTo(new float[bytesBatch.length][1]);
                for (float[] score : scores) {
                    System.out.println("score: " + Arrays.toString(score));
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.println("cost " + cost + " millis");

            TimeUnit.SECONDS.sleep(1L);
        }
    }

    private static byte[] buildExample() {
        Features.Builder featuresBuilder = Features.newBuilder();
        Int64List.Builder int64ListBuilder = Feature.newBuilder().getInt64ListBuilder();
        int64ListBuilder.addValue(ThreadLocalRandom.current().nextInt(100));
        Feature feature = Feature.newBuilder()
                .setInt64List(int64ListBuilder)
                .build();
        featuresBuilder.putFeature("ipcity", feature);
        return Example.newBuilder()
                .setFeatures(featuresBuilder.build())
                .build()
                .toByteArray();
    }

}
