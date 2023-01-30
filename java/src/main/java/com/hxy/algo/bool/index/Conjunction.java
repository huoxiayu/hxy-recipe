package com.hxy.algo.bool.index;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * conjunction，一些布尔条件的组合（clauseList之间为&关系）
 * 一个item的clauseList可以对应为一个conjunction
 * 通过一个conjunction也可以选出一组item
 */
@Getter
public class Conjunction {

    public static final Comparator<Conjunction> COMPARATOR = Comparator.comparing(Conjunction::getId);

    private final int id;
    private final List<Clause> clauseList;
    private final int includeClauseSize;
    private final List<Item> itemList = new ArrayList<>();
    private final Set<Attribute> excludeAttributes;

    public Conjunction(int id, List<Clause> clauseList) {
        this.id = id;
        this.clauseList = clauseList;
        this.includeClauseSize = clauseList.stream()
            .filter(Clause::isInclude)
            .mapToInt(c -> 1)
            .sum();
        this.excludeAttributes = clauseList.stream()
            .filter(Clause::isExclude)
            .flatMap(clause -> clause.getAttributeList().stream())
            .collect(Collectors.toSet());
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    @Override
    public String toString() {
        return "Conjunction(" +
            "id=" + id +
            ", clauseList=" + Clause.clauseList2String(clauseList) +
            ", itemList=" + itemList +
            ')';
    }
}
