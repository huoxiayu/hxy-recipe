package com.hxy.algo.indexbooleanexpression;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * conjunction，一些布尔条件的组合(and关系)
 * 一个item的clauseList可以对应为一个conjunction
 * 通过一个conjunction也可以选出一组item
 */
@Getter
public class Conjunction {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private static final Map<String, Conjunction> CLAUSE_LIST_2_CONJUNCTION = new HashMap<>();

    private final int id;
    private final List<Clause> clauseList;
    private final int includeClauseSize;
    private final List<Item> itemList = new ArrayList<>();

    public Conjunction(int id, List<Clause> clauseList) {
        this.id = id;
        this.clauseList = clauseList;
        this.includeClauseSize = CollectionUtils.isEmpty(clauseList) ? 0 : clauseList.stream()
            .filter(Clause::isInclude)
            .mapToInt(c -> 1)
            .sum();
    }

    public int includeClauseSize() {
        return clauseList.size();
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public static void reset() {
        ID_GENERATOR.set(0);
        CLAUSE_LIST_2_CONJUNCTION.clear();
    }

    public static Conjunction getConjunctionByClauseList(List<Clause> clauseList) {
        String clauses = Objects.toString(CollectionUtils.isNotEmpty(clauseList) ? clauseList : null);
        return CLAUSE_LIST_2_CONJUNCTION.computeIfAbsent(clauses, k -> new Conjunction(ID_GENERATOR.incrementAndGet(), clauseList));
    }

}
