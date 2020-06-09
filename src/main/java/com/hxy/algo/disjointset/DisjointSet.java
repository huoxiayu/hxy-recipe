package com.hxy.algo.disjointset;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 给定一些定向条件，每个条件有多个值，如：[[1, 2, 3, 4], [1, 2], [3], [4]]]
 * 生成最少数量的定向条件，使得每个值在且仅在一个定向条件中，如：[[1, 2], [3], [4]]
 */
@Slf4j
public class DisjointSet {

    public static void main(String[] args) {
        run(List.of(
            List.of(1, 2, 3, 4),
            List.of(1, 2),
            List.of(3),
            List.of(4)
        ));

        run(List.of(
            List.of(1, 2),
            List.of(2, 3),
            List.of(3, 4)
        ));

        List<List<Integer>> bigConditionList = bigConditionList();
        log.info("cost {} millis", BenchmarkUtil.singleRun(
            RunnableUtil.loopRunnable(() -> generateV1(bigConditionList), 10000))
        );
        log.info("cost {} millis", BenchmarkUtil.singleRun(
            RunnableUtil.loopRunnable(() -> generateV2(bigConditionList), 10000))
        );
    }

    private static List<List<Integer>> bigConditionList() {
        int conditions = 10000;
        int values = 10;
        List<List<Integer>> conditionList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < conditions; i++) {
            Set<Integer> condition = new HashSet<>();
            for (int j = 0; j < values; j++) {
                condition.add(random.nextInt(values));
            }
            conditionList.add(new ArrayList<>(condition));
        }
        return conditionList;
    }

    private static void run(List<List<Integer>> conditionList) {
        log.info("conditionList: {}", conditionList);

        List<List<Integer>> resultV1 = generateV1(conditionList);
        resultV1.sort(Comparator.comparing(Object::toString));
        log.info("resultV1: {}", resultV1);

        List<List<Integer>> resultV2 = generateV2(conditionList);
        resultV2.sort(Comparator.comparing(Object::toString));
        log.info("resultV2: {}", resultV2);

        Assert.assertEquals(resultV1, resultV2);
    }

    private static List<List<Integer>> generateV1(List<List<Integer>> conditionList) {
        Map<Integer, Set<Integer>> value2IndexSet = new HashMap<>();
        for (int i = 0; i < conditionList.size(); i++) {
            List<Integer> condition = conditionList.get(i);
            int idx = i;
            condition.forEach(value -> value2IndexSet.computeIfAbsent(value, k -> new HashSet<>()).add(idx));
        }

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> allValues = new ArrayList<>(value2IndexSet.keySet());
        int size = allValues.size();
        for (int i = 0; i < size; i++) {
            Integer valueI = allValues.get(i);
            if (valueI == null) {
                continue;
            }

            List<Integer> collectionInThisRound = new ArrayList<>();
            collectionInThisRound.add(valueI);
            Set<Integer> setForI = value2IndexSet.get(valueI);

            for (int j = i + 1; j < size; j++) {
                Integer valueJ = allValues.get(j);
                if (valueJ == null) {
                    continue;
                }

                Set<Integer> setForJ = value2IndexSet.get(valueJ);
                if (setForI.size() == setForJ.size() && setForI.containsAll(setForJ)) {
                    collectionInThisRound.add(valueJ);
                    allValues.set(j, null);
                }
            }

            result.add(collectionInThisRound);
        }

        return result;
    }

    private static List<List<Integer>> generateV2(List<List<Integer>> conditionList) {
        // 值 -> 存放值的集合
        Map<Integer, BitSet> value2BitSet = new HashMap<>();
        for (int i = 0; i < conditionList.size(); i++) {
            List<Integer> condition = conditionList.get(i);
            for (Integer value : condition) {
                BitSet bitSet = value2BitSet.computeIfAbsent(value, k -> new BitSet());
                // 第i个集合包含value
                bitSet.set(i);
            }
        }

        // 按BitSet collect到一起
        Map<BitSet, List<Integer>> collect = new HashMap<>();
        value2BitSet.forEach((value, bitSet) -> collect.computeIfAbsent(bitSet, k -> new ArrayList<>()).add(value));
        return new ArrayList<>(collect.values());
    }

}
