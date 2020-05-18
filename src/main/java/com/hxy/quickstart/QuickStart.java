package com.hxy.quickstart;

import java.util.concurrent.ThreadLocalRandom;

public class QuickStart {

    public static void main(String[] args) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < 100000; i++) {
            int rand = ThreadLocalRandom.current().nextInt(1, 10);
            min = Math.min(min, rand);
            max = Math.max(max, rand);
        }

        System.out.println("max: " + max);
        System.out.println("min: " + min);
    }

}
