package com.hxy.algo.bool.index;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 同类别PostingList的Holder
 */
public class PostingListsHolder implements Comparable<PostingListsHolder> {

    private final MultiOrderlyConjunctionList conjunctions = MultiOrderlyConjunctionList.Factory.newConjunctionListContainer();
    private Set<Integer> excludeConjunctionIdSet = new HashSet<>();
    private Conjunction currentConjunction;

    public PostingListsHolder(List<PostingList> postingLists) {
        postingLists.forEach(postingList -> {
            Attribute attribute = postingList.getAttribute();
            List<Conjunction> conjunctionList = postingList.getConjunctionList();
            conjunctions.addOrderlyConjunctionList(conjunctionList);
            conjunctionList.forEach(conjunction -> {
                Set<Attribute> excludeAttributes = conjunction.getExcludeAttributes();
                if (excludeAttributes.contains(attribute)) {
                    excludeConjunctionIdSet.add(conjunction.getId());
                }
            });
        });
        currentConjunction = conjunctions.first();
    }

    public Conjunction currentConjunction() {
        return currentConjunction;
    }

    public void nextGreaterThenOrEqualTo(Conjunction current) {
        if (currentConjunction != null && currentConjunction.getId() < current.getId()) {
            currentConjunction = conjunctions.nextGreaterThenOrEqualTo(current);
        }
    }

    public boolean isExcludeOnConjunctionId(int conjunctionId) {
        return excludeConjunctionIdSet.contains(conjunctionId);
    }

    /**
     * PostingListsHolder内部维护的conjunctionList按照conjunction的id从小到大排序
     * PostingListsHolder之间的排序规则：
     * 1、按current-conjunction的id从小到大排序，current-conjunction为当前迭代到的conjunction，初始情况下是第一个conjunction
     * 2、current-conjunction的id相同时，条件为exclude的排前面（用于代码中过滤掉exclude的情况）
     * 3、current-conjunction为null的排后面（用于代码中快速判断不能满足matchCategorySize个类别条件）
     */
    @Override
    public int compareTo(PostingListsHolder o) {
        Conjunction current = this.currentConjunction();
        Conjunction other = o.currentConjunction();
        if (current == null) {
            return 1;
        } else if (other == null) {
            return -1;
        } else {
            int currentId = current.getId();
            int otherId = other.getId();
            int cmp = Integer.compare(currentId, otherId);
            if (cmp == 0) {
                if (isExcludeOnConjunctionId(currentId)) {
                    return -1;
                } else if (o.isExcludeOnConjunctionId(otherId)) {
                    return 1;
                }
            }
            return cmp;
        }
    }
}
