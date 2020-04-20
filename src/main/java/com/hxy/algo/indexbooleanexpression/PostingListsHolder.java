package com.hxy.algo.indexbooleanexpression;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class PostingListsHolder implements Comparable<PostingListsHolder> {

    private final TreeSet<Conjunction> conjunctions = new TreeSet<>(Comparator.comparing(Conjunction::getId));
    private Conjunction currentConjunction;

    public PostingListsHolder(List<PostingList> postingLists) {
        postingLists.forEach(postingList -> conjunctions.addAll(postingList.getConjunctionList()));
        currentConjunction = conjunctions.first();
    }

    public Conjunction currentConjunction() {
        return currentConjunction;
    }

    public void next(Conjunction current) {
        currentConjunction = conjunctions.ceiling(current);
    }

    @Override
    public int compareTo(PostingListsHolder o) {
        Conjunction current = this.currentConjunction();
        Conjunction other = o.currentConjunction();
        if (current == null) {
            return 1;
        } else if (other == null) {
            return -1;
        } else {
            return Integer.compare(current.getId(), other.getId());
        }
    }
}
