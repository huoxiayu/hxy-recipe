package com.hxy.leetcode;

import java.util.HashMap;
import java.util.Map;

public class Solution {

    public int[] twoSum(int[] nums, int target) {
        int length = nums.length;
        Map<Integer, Integer> value2Idx = new HashMap<>(length);
        for (int i = 0; i < nums.length; i++) {
            value2Idx.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            Integer needIdx = value2Idx.get(need);
            if (needIdx != null && needIdx != i) {
                return new int[]{i, needIdx};
            }
        }

        return null;
    }

    public int multiply(int A, int B) {
        if (A == 0 || B == 0) {
            return 0;
        }

        boolean negative = (A < 0 && B > 0) || (A > 0 && B < 0);

        A = Math.abs(A);
        B = Math.abs(B);

        int sum = 0;
        int base = A;
        for (int i = 0; i < 32; i++) {
            if ((B & (1 << i)) != 0) {
                sum += base;
            }
            base += base;
        }

        return negative ? -sum : sum;
    }

    public int[] replaceElements(int[] arr) {
        int length = arr.length;
        int[] result = new int[length];
        int maxFromRight2Left = Integer.MIN_VALUE;
        for (int i = length - 1; i >= 0; i--) {
            result[i] = maxFromRight2Left;
            maxFromRight2Left = Math.max(maxFromRight2Left, arr[i]);
        }

        result[length - 1] = -1;
        return result;
    }

    public int dominantIndex(int[] nums) {
        if (nums.length == 0) {
            return -1;
        }

        if (nums.length == 1) {
            return 0;
        }

        int firstMax = 0;
        int secondMax = 1;
        if (nums[firstMax] < nums[secondMax]) {
            firstMax = 1;
            secondMax = 0;
        }

        for (int i = 2; i < nums.length; i++) {
            if (nums[i] > nums[firstMax]) {
                secondMax = firstMax;
                firstMax = i;
            } else if (nums[i] > nums[secondMax]) {
                secondMax = i;
            }
        }

        return nums[firstMax] >= 2 * nums[secondMax] ? firstMax : -1;
    }

    public void moveZeroes(int[] nums) {

    }

    public static void main(String[] args) {
        System.out.println(new Solution().dominantIndex(new int[]{1, 0}));
    }

}