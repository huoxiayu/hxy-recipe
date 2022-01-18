package com.hxy.recipe.bitop;

public class BitOpStart {

    public static void main(String[] args) {
        int i = -1;
        System.out.println(i + "\t" + Integer.toBinaryString(i));

        // 符号右移
        int iShift = i >> 1;
        System.out.println(iShift + "\t" + Integer.toBinaryString(iShift));

        // 补0右移
        int iShiftNoSign = i >>> 1;
        System.out.println(iShiftNoSign + "\t" + Integer.toBinaryString(iShiftNoSign));
    }

}
