package com.hxy.ml.tensorflow;

import com.google.common.base.Preconditions;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.tensorflow.example.Example;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExampleStart {

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/hxy/data/examples");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        List<byte[][]> deserializeList = (List<byte[][]>) ois.readObject();
        log.info("deserializeList.size() -> {}", deserializeList.size());
        List<byte[]> examples = deserializeList.stream()
                .flatMap(Arrays::stream)
                .sorted(Comparator.comparingInt(b -> b.length))
                .collect(Collectors.toList());
        log.info("examples.size() -> {}", examples.size());
        log.info("examples.bytes() -> {}", examples.stream().mapToInt(b -> b.length).sum());

        byte[] first = examples.get(0);
        print(first);

        byte[] last = examples.get(examples.size() - 1);
        print(last);

        int max = 10000;
        for (int k = 0; k < 4; k++) {
            new Thread(() -> {
                LongArrayList list = new LongArrayList(max);
                while (true) {
                    for (int i = 0; i < max; i++) {
                        long start = System.currentTimeMillis();

                        for (int x = 0; x < 10; x++) {
                            Example example;
                            try {
                                example = Example.parseFrom(first);
                                byte[] bytes = example.toByteArray();
                                Preconditions.checkState(bytes.length > 0);
                            } catch (InvalidProtocolBufferException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        long cost = System.currentTimeMillis() - start;
                        list.add(cost);
                    }

                    list.sortThis();
                    double p = 0.99;
                    log.info("p99 cost {} millis", list.get((int) (max * p)));

                    list.clear();
                    System.gc();
                }

            }).start();
        }
    }

    public static void print(byte[] b) throws Exception {
        log.info("bytes -> {}", b.length);
        Example example = Example.parseFrom(b);
        log.info("example -> {}", example);
        log.info("humanSize -> {}", RamUsageEstimator.humanSizeOf(example));
    }

}
