package com.hxy.algo.bool.index;

import java.util.List;
import java.util.Set;

public class ComposeSet<E> {

    private List<Set<E>> sets;

    public ComposeSet(List<Set<E>> sets) {
        this.sets = sets;
    }

    public boolean contains(E e) {
        return sets.stream().anyMatch(set -> set.contains(e));
    }

}
