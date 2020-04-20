package com.hxy.algo.indexbooleanexpression;

import lombok.ToString;

import java.util.List;

@ToString
public class ItemImpl implements Item {

    @ToString.Include
    private final String id;
    @ToString.Exclude
    private final List<Clause> clauseList;

    public ItemImpl(String id, List<Clause> clauseList) {
        this.id = id;
        this.clauseList = clauseList;
    }

    @Override
    public List<Clause> clauseList() {
        return clauseList;
    }

}