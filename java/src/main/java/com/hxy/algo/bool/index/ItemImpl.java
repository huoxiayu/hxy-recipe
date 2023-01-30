package com.hxy.algo.bool.index;

import java.util.List;

public class ItemImpl implements Item {

    private final String id;
    private final List<Clause> clauseList;

    public ItemImpl(String id, List<Clause> clauseList) {
        this.id = id;
        this.clauseList = clauseList;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public List<Clause> clauseList() {
        return clauseList;
    }

    @Override
    public String toString() {
        return id();
    }

}