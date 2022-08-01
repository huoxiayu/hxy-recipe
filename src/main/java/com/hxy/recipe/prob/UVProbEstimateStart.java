package com.hxy.recipe.prob;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class UVProbEstimateStart {

    private static final int TIMES = 1000;

    public static void main(String[] args) {
        PredictInfo predictInfo = predictInfo();
        estimateUv(predictInfo);
    }

    private static PredictInfo predictInfo() {
        Set<String> idSet = new HashSet<>();
        int total = 100_000;
        for (int i = 0; i < total; i++) {
            idSet.add(String.valueOf(i));
        }
        log.info("idSet.size() -> {}", idSet.size());

        Set<String> leftSet = new HashSet<>();
        for (int i = 0; i < 5_000; i++) {
            leftSet.add(String.valueOf(i));
        }
        log.info("leftSet.size() -> {}", leftSet.size());

        Set<String> rightSet = new HashSet<>();
        for (int i = 0; i < 7_000; i++) {
//        for (int i = 3000; i < 9_000; i++) {
            rightSet.add(String.valueOf(i));
        }
        log.info("rightSet.size() -> {}", rightSet.size());

        MutableInt sum = new MutableInt(0);
        leftSet.forEach(l -> {
            if (rightSet.contains(l)) {
                sum.increment();
            }
        });
        log.info("join.size() -> {}", sum.intValue());

        double leftProb = 0.5D;
        double rightProb = 0.5D;
        log.info("leftProb -> {}, rightProb -> {}", leftProb, rightProb);

        return new PredictInfo(
                idSet, leftSet, leftProb, rightSet, rightProb
        );
    }

    private static void estimateUv(PredictInfo predictInfo) {
        Set<String> leftSet = predictInfo.getLeftSet();
        double leftProb = predictInfo.getLeftProb();
        Set<String> rightSet = predictInfo.getRightSet();
        double rightProb = predictInfo.getRightProb();

        Set<String> merge = new HashSet<>();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        int[] result = new int[TIMES];
        for (int i = 0; i < TIMES; i++) {
            leftSet.forEach(left -> {
                if (rand.nextDouble() < leftProb) {
                    merge.add(left);
                }
            });
            rightSet.forEach(right -> {
                if (rand.nextDouble() < rightProb) {
                    merge.add(right);
                }
            });


            result[i] = merge.size();
            merge.clear();
        }

        IntSummaryStatistics retStatistics = Arrays.stream(result).summaryStatistics();
        log.info("retStatistics -> {}", retStatistics);
    }

    @Data
    private static class PredictInfo {
        private final Set<String> idSet;
        private final Set<String> leftSet;
        private final double leftProb;
        private final Set<String> rightSet;
        private final double rightProb;
    }

}
