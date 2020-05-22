package com.hxy.algo.bool.index;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * 最简单的TreeSet实现
 * 每次将conjunctionList加到treeSet中，存在大量add操作，效率较低
 * 实现简单，可以直接利用treeSet的api
 * 不推荐使用
 */
public class TreeSetMultiOrderlyConjunctionList implements MultiOrderlyConjunctionList {

    static {
        System.out.println("TreeSetMultiOrderlyConjunctionList");
    }

    private final TreeSet<Conjunction> conjunctions = new TreeSet<>(Conjunction.COMPARATOR);

    // 如果conjunctionList较长可能非常耗时
    @Override
    public void addOrderlyConjunctionList(List<Conjunction> conjunctionList) {
        conjunctions.addAll(conjunctionList);
    }

    @Override
    public Conjunction first() {
        return conjunctions.first();
    }

    @Override
    public Conjunction nextGreaterThenOrEqualTo(Conjunction conjunction) {
        return conjunctions.ceiling(conjunction);
    }

}
