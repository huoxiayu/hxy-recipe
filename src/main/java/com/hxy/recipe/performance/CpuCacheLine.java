package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;

public class CpuCacheLine {

    public static void main(String[] args) {
        cpuCache();
    }

    private static void cpuCache() {
        int n = 4096;
        int m = 8192;
        int[][] twoDimArray = new int[n][m];

        long cost1 = BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    twoDimArray[i][j] = 1;
                }
            }
        });
        System.out.println(cost1);  // 33

        long cost2 = BenchmarkUtil.singleRun(() -> {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    twoDimArray[i][j] = 1;
                }
            }
        });
        System.out.println(cost2);  // 530
    }

}
