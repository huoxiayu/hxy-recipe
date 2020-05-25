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
            Conjunction conjunction = getConjunctionByClauseList(clauseList);
            conjunction.addItem(item);
            Map<Attribute, PostingList> attribute2PostingList = size2Att2PostingList.computeIfAbsent(
                conjunction.getIncludeClauseSize(),
                k -> new HashMap<>()
            );
            clauseList.forEach(clause -> {
                List<Attribute> attributeList = clause.getAttributeList();
                attributeList.forEach(attribute -> {
                    PostingList postingList = attribute2PostingList.computeIfAbsent(
                        attribute, k -> new PostingList(attribute)
                    );
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

        /**
         * 这里只讨论一个Item有一组条件的情况（clauseList代表一组条件），多组条件的情况可以拆成多个Item
         * 因此这里一个Item只会对应到一个conjunction，在collect时不需要去重
         * @see Item#clauseList()
         */
        List<Item> result = conjunctionList.stream()
            .flatMap(conjunction -> conjunction.getItemList().stream())
            .collect(Collectors.toList());
        log.debug("result: {}", result);
        return result;
    }

    private List<Conjunction> getConjunctionListByAssignment(Assignment assignment) {
        List<Conjunction> conjunctions = new ArrayList<>();
        List<Attribute> attributesInAssignment = assignment.getAttributeList();
        int attributeCategoryCount = attributesInAssignment.stream()
            .map(Attribute::getCategory)
            .distinct()
            .mapToInt(c -> 1)
            .sum();
        // 每一轮迭代，求出满足matchCategorySize个类别定向的conjunctionList
        for (int matchCategorySize = Math.min(maxConjunctionSize, attributeCategoryCount); matchCategorySize > 0; matchCategorySize--) {
            Map<Attribute, PostingList> att2PostingList = size2Att2PostingList.get(matchCategorySize);
            if (MapUtils.isEmpty(att2PostingList)) {
                continue;
            }

            Map<AttributeCategory, List<PostingList>> category2PostingLists = new HashMap<>();
            attributesInAssignment.forEach(att -> {
                PostingList postingList = att2PostingList.get(att);
                if (postingList != null) {
                    category2PostingLists.computeIfAbsent(att.getCategory(), k -> new ArrayList<>()).add(postingList);
                }
            });

            // 按类别collect到一起，不满足matchCategorySize则跳过本轮迭代
            if (category2PostingLists.keySet().size() < matchCategorySize) {
                continue;
            }

            List<PostingListsHolder> postingListsHolders = category2PostingLists.values()
                .stream()
                .map(PostingListsHolder::new)
                .collect(Collectors.toList());

            List<Conjunction> matchConjunctionList = getMatchConjunctions(matchCategorySize, postingListsHolders);
            conjunctions.addAll(matchConjunctionList);
        }

        return conjunctions;
    }

    /**
     * postingListsHolders.size() >= conjunctionSize
     *
     * @see PostingListsHolder#compareTo(PostingListsHolder)
     */
    private List<Conjunction> getMatchConjunctions(int matchCategorySize, List<PostingListsHolder> postingListsHolders) {
        List<Conjunction> conjunctions = new ArrayList<>();
        int lastIdx = matchCategorySize - 1;
        for (; ; ) {
            Collections.sort(postingListsHolders);

            PostingListsHolder lastPostingList = postingListsHolders.get(lastIdx);
            Conjunction last = lastPostingList.currentConjunction();
            // 不能满足matchCategorySize个条件，提前退出
            if (last == null) {
                break;
            }

            PostingListsHolder firstPostingList = postingListsHolders.get(0);
            Conjunction first = firstPostingList.currentConjunction();
            int firstId = first.getId();
            int lastId = last.getId();

            // 这里使用AtomicReference是为了允许在lambda中进行set(update)操作
            AtomicReference<Conjunction> nextRef = new AtomicReference<>();
            if (firstId == lastId) {
                boolean excludeOnConjunctionId = firstPostingList.isExcludeOnConjunctionId(firstId);
                if (!excludeOnConjunctionId) {
                    conjunctions.add(first);
                } else {
                    log.debug("postListingHolder {} exclude {}", firstPostingList, firstId);
                }
                // 下一轮迭代的conjunctionId至少为firstId+1
                nextRef.set(new Conjunction(firstId + 1, Collections.emptyList()));
            } else {
                nextRef.set(last);
            }

            postingListsHolders.forEach(postingListsHolder -> postingListsHolder.nextGreaterThenOrEqualTo(nextRef.get()));
        }

        return conjunctions;
    }

    public void show() {
        size2Att2PostingList.forEach((size, att2PostingList) -> {
            log.info("{} -> {}", size, att2PostingList.entrySet().toString());
        });
    }

}