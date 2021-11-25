package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;

// cpu缓存对性能的影响
public class CpuCacheLine {

    public static void main(String[] args) {
        cpuCache();

        falseSharing();
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
        System.out.println("cache line hit cost " + cost1);  // 33

        long cost2 = BenchmarkUtil.singleRun(() -> {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    twoDimArray[i][j] = 1;
                }
            }
        });
        System.out.println("cache line miss cost " + cost2);  // 530
    }

    private static void falseSharing() {
        int times = 1_0000_0000;
        T[] tList = new T[2];
        tList[0] = new T();
        tList[1] = new T();

        Thread write1 = new Thread(() -> {
            long cost = BenchmarkUtil.singleRun(() -> {
                for (int i = 0; i < times; i++) {
                    tList[0].v = i;
                }
            });
            System.out.println("write1 cost -> " + cost);
        });

        Thread write2 = new Thread(() -> {
            long cost = BenchmarkUtil.singleRun(() -> {
                for (int i = 0; i < times; i++) {
                    tList[1].v = i;
                }
            });
            System.out.println("write2 cost -> " + cost);
        });

        write1.start();
        write2.start();
    }

    // @jdk.internal.vm.annotation.Contended
    private static class T {

        private long p1, p2, p3, p4, p5, p6, p7 = 0L;

        private volatile long v = 0L;

        private long p11, p12, p13, p14, p15, p16, p17 = 0L;

    }

}
