package com.hxy.algo.bool.index;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class PostingListsHolder implements Comparable<PostingListsHolder> {

    private final TreeSet<Conjunction> conjunctions = new TreeSet<>(Comparator.comparing(Conjunction::getId));
    private Set<Integer> excludeConjunctionIdSet = new HashSet<>();
    private Conjunction currentConjunction;

    public PostingListsHolder(List<PostingList> postingLists) {
        postingLists.forEach(postingList -> {
            Attribute attribute = postingList.getAttribute();
            List<Conjunction> conjunctionList = postingList.getConjunctionList();
            conjunctions.addAll(conjunctionList);
            conjunctionList.forEach(conjunction -> {
                Set<Attribute> excludeAttributes = conjunction.getExcludeAttributes();
                if (excludeAttributes.contains(attribute)) {
                    excludeConjunctionIdSet.add(conjunction.getId());
                    log.info("postingListHolder {} conjunction {} exclude attribute {}", this, conjunction.getId(), attribute);
                }
            });
        });
        currentConjunction = conjunctions.first();
    }

    public Conjunction currentConjunction() {
        return currentConjunction;
    }

    public void next(Conjunction current) {
        currentConjunction = conjunctions.ceiling(current);
    }

    public boolean isExcludeOnConjunctionId(int conjunctionId) {
        return excludeConjunctionIdSet.contains(conjunctionId);
    }

    @Override
    public int compareTo(PostingListsHolder o) {
        Conjunction current = this.currentConjunction();
        Conjunction other = o.currentConjunction();
        // null排后面
        if (current == null) {
            return 1;
        } else if (other == null) {
            return -1;
        } else {
            int currentId = current.getId();
            int otherId = other.getId();
            int cmp = Integer.compare(currentId, otherId);
            if (cmp == 0) {
                // exclude排前面
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
