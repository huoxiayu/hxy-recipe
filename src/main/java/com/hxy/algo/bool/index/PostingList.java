package com.hxy.algo.bool.index;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostingList {

    private final Attribute attribute;
    private final List<Conjunction> conjunctionList;

    public PostingList(Attribute attribute) {
        this.attribute = attribute;
        this.conjunctionList = new ArrayList<>();
    }

    public void addConjunction(Conjunction conjunction) {
        conjunctionList.add(conjunction);
    }

    @Override
    public String toString() {
        String conjunctionIdList = conjunctionList.stream()
            .map(Conjunction::getId)
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        return String.format("PostingList(%s => %s)", attribute, conjunctionIdList);
    }
}
