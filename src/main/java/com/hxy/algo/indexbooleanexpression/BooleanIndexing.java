package com.hxy.algo.indexbooleanexpression;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 布尔表达式的倒排索引
 * 维护了conjunctionSize -> attribute -> conjunction的postingList的映射
 * 可以根据流量画像Assignment快速检索Item对象
 */
@Slf4j
public class BooleanIndexing {

    private final Map<Integer, Map<Attribute, PostingList>> size2Att2PostingList;
    private final int maxConjunctionSize;

    public BooleanIndexing(List<Item> itemList) {
        Conjunction.reset();
        size2Att2PostingList = new HashMap<>();
        itemList.forEach(item -> {
            List<Clause> clauseList = item.clauseList();
            Conjunction conjunction = Conjunction.getConjunctionByClauseList(clauseList);
            conjunction.addItem(item);
            int includeClauseSize = conjunction.includeClauseSize();
            Map<Attribute, PostingList> attribute2PostingList = size2Att2PostingList.computeIfAbsent(includeClauseSize, k -> new HashMap<>());
            clauseList.forEach(clause -> clause.getAttributeList().forEach(attribute -> {
                PostingList postingList = attribute2PostingList.computeIfAbsent(attribute, k -> new PostingList());
                postingList.addConjunction(conjunction);
            }));
        });
        maxConjunctionSize = size2Att2PostingList.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public List<Item> getItemsByAssignment(Assignment assignment) {
        log.debug("assignment: {}", assignment);
        List<Conjunction> conjunctionList = getConjunctionListByAssignment(assignment);
        if (CollectionUtils.isEmpty(conjunctionList)) {
            return Collections.emptyList();
        }

        List<Item> result = conjunctionList.stream()
            .flatMap(conjunction -> conjunction.getItemList().stream())
            .collect(Collectors.toList());
        log.debug("result: {}", result);
        return result;
    }

    private List<Conjunction> getConjunctionListByAssignment(Assignment assignment) {
        List<Conjunction> conjunctions = new ArrayList<>();
        List<Attribute> attributeList = assignment.getAttributeList();
        for (int conjunctionSize = Math.min(maxConjunctionSize, attributeList.size()); conjunctionSize >= 0; conjunctionSize--) {
            Map<Attribute, PostingList> att2PostingList = size2Att2PostingList.get(conjunctionSize);
            if (MapUtils.isEmpty(att2PostingList)) {
                continue;
            }

            Map<AttributeCategory, List<PostingList>> category2PostingListS = new HashMap<>();
            attributeList.forEach(att -> {
                PostingList postingList = att2PostingList.get(att);
                if (postingList != null) {
                    category2PostingListS.computeIfAbsent(att.getCategory(), k -> new ArrayList<>()).add(postingList);
                }
            });

            if (category2PostingListS.keySet().size() < conjunctionSize) {
                continue;
            }

            List<PostingListsHolder> postingListsHolders = category2PostingListS.values()
                .stream()
                .map(PostingListsHolder::new)
                .collect(Collectors.toList());
            conjunctions.addAll(getMatchConjunctions(conjunctionSize, postingListsHolders));
        }

        return conjunctions;
    }

    private List<Conjunction> getMatchConjunctions(int conjunctionSize, List<PostingListsHolder> postingListsHolders) {
        List<Conjunction> conjunctions = new ArrayList<>();
        Collections.sort(postingListsHolders);
        while (postingListsHolders.get(conjunctionSize - 1).currentConjunction() != null) {
            Conjunction first = postingListsHolders.get(0).currentConjunction();
            Conjunction last = postingListsHolders.get(conjunctionSize - 1).currentConjunction();
            AtomicReference<Conjunction> nextRef = new AtomicReference<>();
            if (first.getId() == last.getId()) {
                conjunctions.add(first);
                nextRef.set(new Conjunction(first.getId() + 1, null));
            } else {
                nextRef.set(last);
            }

            postingListsHolders.forEach(postingListsHolder -> postingListsHolder.next(nextRef.get()));
            Collections.sort(postingListsHolders);
        }
        return conjunctions;
    }

}