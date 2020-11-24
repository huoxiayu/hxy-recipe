package com.hxy.recipe.datastructure.hyperloglog;

import com.google.zetasketch.HyperLogLogPlusPlus;
import com.hxy.recipe.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZetaSketchHyperLogLogStart {

    public static void main(String[] args) {
        int size = 100_0000;
        int[] sortedIntArray = RandomUtil.sortedIntArray(size);
        List<String> dataSet = new ArrayList<>(size);
        for (int data : sortedIntArray) {
            dataSet.add(String.valueOf(data));
        }

        HyperLogLogPlusPlus<String> hll1 = newHyperLogLog();
        dataSet.stream().filter((data -> Integer.parseInt(data) % 2 == 0)).forEach(hll1::add);
        log.info("hll1 {}", hll1.longResult());

        HyperLogLogPlusPlus<String> hll2 = newHyperLogLog();
        dataSet.stream().filter((data -> Integer.parseInt(data) % 2 == 1)).forEach(hll2::add);
        log.info("hll2 {}", hll2.longResult());

        hll1.merge(hll2);
        log.info("hll1 after merge {}", hll1.longResult());
    }

    private static HyperLogLogPlusPlus<String> newHyperLogLog() {
        return new HyperLogLogPlusPlus
            .Builder()
            .normalPrecision(20)
            .sparsePrecision(20)
            .buildForStrings();
    }

}
