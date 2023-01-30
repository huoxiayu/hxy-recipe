package com.hxy.recipe.compute;

import com.hxy.recipe.util.Utils;

import java.util.Arrays;

public class ComputeStart {

    public static void main(String[] args) {
        int[] intArray = new int[1024];
        Arrays.fill(intArray, 1);

        for (int i = 0; i < 100; i++) {
            calc(intArray);
        }

        Utils.sleepInSeconds(10L);

        calc(intArray);
    }

    private static void calc(int[] intArray) {
        long start = System.currentTimeMillis();
        int sum = 0;
        int i = 0;
        while(i < 100_0000) {
            int j = 0;
            while(j < 1024) {
                sum += intArray[j];
                j++;
            }

            i++;
        }

        long cost = System.currentTimeMillis() - start;
        System.out.println("cost: " + cost + " sum: " + sum);
    }

}
