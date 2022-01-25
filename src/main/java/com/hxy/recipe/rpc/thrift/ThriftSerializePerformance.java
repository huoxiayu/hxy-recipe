package com.hxy.recipe.rpc.thrift;

import com.hxy.recipe.thrift.A;
import com.hxy.recipe.thrift.B;
import com.hxy.recipe.thrift.BigObject;
import com.hxy.recipe.thrift.BigObjectContainer;
import com.hxy.recipe.thrift.C;
import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * -XX:+UnlockDiagnosticVMOptions
 * -XX:+PrintInlining
 * <p>
 * 强制内联
 * -XX:CompileCommand=inline,com/hxy/recipe/rpc/thrift/*.*
 */
@Slf4j
public class ThriftSerializePerformance {

    private static int CNT = 0;
    private static long SE_SUM_COST = 0L;
    private static long DE_SUM_COST = 0L;

    @SneakyThrows
    public static void main(String[] args) {
        BigObjectContainer bigObjectContainer = bigObjectContainer();
        log.info("humanSize -> {}", RamUsageEstimator.humanSizeOf(bigObjectContainer));

        run(bigObjectContainer);
        run(bigObjectContainer);
        run(bigObjectContainer);
        Utils.sleepInSeconds(10L);

        CNT = 0;
        SE_SUM_COST = 0L;
        DE_SUM_COST = 0L;

        while (true) {
            System.gc();
            run(bigObjectContainer);
            Utils.sleepInSeconds(1L);
            log.info("cnt -> {}, se -> {}, de -> {}", CNT, SE_SUM_COST, DE_SUM_COST);
        }
    }

    private static BigObjectContainer bigObjectContainer() {
        BigObjectContainer bigObjectContainer = new BigObjectContainer();
        List<BigObject> bigObjectList = new ArrayList<>();
        int size = 100000;
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        List<Integer> intList = IntStream.range(0, 1000)
                .boxed()
                .collect(Collectors.toList());
        for (int i = 0; i < size; i++) {
            BigObject bigObject = new BigObject();
            A a = new A();
            B b = new B();
            C c = new C();
            c.setCInt(rand.nextInt());
            c.setCLong(rand.nextLong());
            c.setCIntList(intList);
            b.setC(c);
            b.setBDouble(rand.nextDouble());
            b.setBLong(rand.nextLong());
            a.setB(b);
            a.setABool(true);
            String longString = IntStream.range(0, 25)
                    .mapToObj(any -> UUID.randomUUID().toString())
                    .collect(Collectors.joining("-"));
            a.setAStr(longString);
            bigObject.setAList(List.of(a));
            bigObjectList.add(bigObject);
        }

        bigObjectContainer.setBigObjectList(bigObjectList);
        Map<Integer, Double> i2d = new HashMap<>();
        for (int i = 0; i < size; i++) {
            i2d.put(i, i + 0.1D);
        }
        bigObjectContainer.setI2d(i2d);

        return bigObjectContainer;
    }

    @SneakyThrows
    private static void run(BigObjectContainer bigObjectContainer) {
        AtomicReference<byte[]> bytesRef = new AtomicReference<>();
        long serializeCost = BenchmarkUtil.singleRun(() -> {
            TSerializer tSerializer = new TSerializer();
            try {
                bytesRef.set(tSerializer.serialize(bigObjectContainer));
            } catch (TException ignore) {
            }
        });

        SE_SUM_COST += serializeCost;

        long deserializeCost = BenchmarkUtil.singleRun(() -> {
            TDeserializer tDeserializer = new TDeserializer();
            try {
                tDeserializer.deserialize(bigObjectContainer, bytesRef.get());
            } catch (TException ignore) {

            }
        });

        DE_SUM_COST += deserializeCost;

        CNT++;
    }

}
