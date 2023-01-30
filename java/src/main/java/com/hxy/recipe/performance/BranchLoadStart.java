package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

// 分支预测
public class BranchLoadStart {

    public static void main(String[] args) {
        System.out.println("branch load true cost " + branchLoad(true));    // 2282
        System.out.println("branch load false cost " + branchLoad(false));  // 7875
    }

    private static long branchLoad(boolean branchLoad) {
        int n = 1 << 20;
        int mid = n >> 1;
        int[] array = randomArray(n);
        int times = 1000;
        return BenchmarkUtil.singleRun(() -> {
            if (branchLoad) {
                Arrays.sort(array);

                for (int t = 0; t < times; t++) {
                    for (int i = 0; i < n; i++) {
                        if (array[i] < mid) {
                            array[i] = 0;
                        }
                    }
                }
            } else {
                for (int t = 0; t < times; t++) {
                    for (int i = 0; i < n; i++) {
                        if (array[i] < mid) {
                            array[i] = 0;
                        }
                    }
                }

                Arrays.sort(array);
            }
        });
    }

    private static int[] randomArray(int n) {
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = ThreadLocalRandom.current().nextInt(n);
        }
        return array;
    }

}
