package com.hxy.algo.indexbooleanexpression;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostingList {

    private final List<Conjunction> conjunctionList = new ArrayList<>();

    public void addConjunction(Conjunction conjunction) {
        conjunctionList.add(conjunction);
    }

}
