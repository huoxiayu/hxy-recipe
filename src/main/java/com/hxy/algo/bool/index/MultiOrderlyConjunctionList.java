package com.hxy.algo.bool.index;

import java.util.List;

/**
 * 维护了多个conjunctionList，conjunctionList（id）本身是有序的
 */
public interface MultiOrderlyConjunctionList {

    void addOrderlyConjunctionList(List<Conjunction> conjunctionList);

    Conjunction first();

    Conjunction nextGreaterThenOrEqualTo(Conjunction conjunction);

    interface Factory {
        static MultiOrderlyConjunctionList newConjunctionListContainer() {
            if (Boolean.valueOf(System.getProperty("use.tree.set"))) {
                return new TreeSetMultiOrderlyConjunctionList();
            } else {
                return new LogicComposeConjunctionList();
            }
        }
    }

}
