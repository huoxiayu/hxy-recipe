package com.hxy.algo.bool.index;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 布尔表达式的倒排索引
 * 维护了conjunctionSize -> attribute -> conjunction的postingList的映射
 * 可以根据流量画像Assignment快速检索Item对象
 */
@Slf4j
public class BooleanIndex {

    private final AtomicInteger idGenerator = new AtomicInteger();
    private final Map<String, Conjunction> clauseList2Conjunction = new HashMap<>();
    private final Map<Integer, Map<Attribute, PostingList>> size2Att2PostingList = new HashMap<>();
    private final int maxConjunctionSize;

    public BooleanIndex(List<Item> itemList) {
        itemList.forEach(item -> {
            List<Clause> clauseList = new ArrayList<>(item.clauseList());
            clauseList.add(Clause.DEFAULT_CLAUSE);
            int includeClauseSize = clauseList.stream()
                .filter(Clause::isInclude)
                .mapToInt(c -> 1)
                .sum();
            Conjunction conjunction = getConjunctionByClauseList(clauseList);
            conjunction.addItem(item);
            Map<Attribute, PostingList> attribute2PostingList = size2Att2PostingList.computeIfAbsent(includeClauseSize, k -> new HashMap<>());
            clauseList.forEach(clause -> {
                List<Attribute> attributeList = clause.getAttributeList();
                attributeList.forEach(attribute -> {
                    PostingList postingList = attribute2PostingList.computeIfAbsent(attribute, k -> new PostingList(attribute));
                    postingList.addConjunction(conjunction);
                });
            });
        });
        maxConjunctionSize = size2Att2PostingList.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    private Conjunction getConjunctionByClauseList(List<Clause> clauseList) {
        String clauseListId = Clause.clauseList2String(clauseList);
        return clauseList2Conjunction.computeIfAbsent(clauseListId, k -> new Conjunction(idGenerator.incrementAndGet(), clauseList));
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
        int distinctAttributeCategory = attributeList.stream().map(Attribute::getCategory).distinct().mapToInt(c -> 1).sum();
        for (int conjunctionSize = Math.min(maxConjunctionSize, distinctAttributeCategory); conjunctionSize > 0; conjunctionSize--) {
            Map<Attribute, PostingList> att2PostingList = size2Att2PostingList.get(conjunctionSize);
            if (MapUtils.isEmpty(att2PostingList)) {
                continue;
            }

            Map<AttributeCategory, List<PostingList>> category2PostingLists = new HashMap<>();
            attributeList.forEach(att -> {
                PostingList postingList = att2PostingList.get(att);
                if (postingList != null) {
                    category2PostingLists.computeIfAbsent(att.getCategory(), k -> new ArrayList<>()).add(postingList);
                }
            });

            if (category2PostingLists.keySet().size() < conjunctionSize) {
                continue;
            }

            List<PostingListsHolder> postingListsHolders = category2PostingLists.values()
                .stream()
                .map(PostingListsHolder::new)
                .collect(Collectors.toList());
            conjunctions.addAll(getMatchConjunctions(conjunctionSize, postingListsHolders));
        }

        return conjunctions;
    }

    private List<Conjunction> getMatchConjunctions(int conjunctionSize, List<PostingListsHolder> postingListsHolders) {
        // match [0, lastIdx]
        int lastIdx = conjunctionSize - 1;
        List<Conjunction> conjunctions = new ArrayList<>();
        Collections.sort(postingListsHolders);
        while (true) {
            PostingListsHolder lastPostingList = postingListsHolders.get(lastIdx);
            Conjunction last = lastPostingList.currentConjunction();
            // 不能match conjunctionSize个条件
            if (last == null) {
                break;
            }

            PostingListsHolder firstPostingList = postingListsHolders.get(0);
            Conjunction first = firstPostingList.currentConjunction();
            int firstId = first.getId();
            int lastId = last.getId();

            AtomicReference<Conjunction> nextRef = new AtomicReference<>();
            if (firstId == lastId) {
                boolean excludeOnConjunctionId = firstPostingList.isExcludeOnConjunctionId(firstId);
                if (!excludeOnConjunctionId) {
                    conjunctions.add(first);
                } else {
                    log.info("postListingHolder {} exclude {}", firstPostingList, firstId);
                }
                nextRef.set(new Conjunction(firstId + 1, Collections.emptyList()));
            } else {
                nextRef.set(last);
            }

            postingListsHolders.forEach(postingListsHolder -> postingListsHolder.next(nextRef.get()));
            Collections.sort(postingListsHolders);
        }

        log.info("index size {} => {}", conjunctionSize, conjunctions);
        return conjunctions;
    }

    public void show() {
        size2Att2PostingList.forEach((size, att2PostingList) -> {
            log.info("{} -> {}", size, att2PostingList.entrySet().toString());
        });
    }

}