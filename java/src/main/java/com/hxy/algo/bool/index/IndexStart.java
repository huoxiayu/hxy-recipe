package com.hxy.algo.bool.index;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class IndexStart {

    public static void main(String[] args) {
        Attribute attAge30 = new Attribute("30", AttributeCategory.AGE);
        Attribute attAge50 = new Attribute("50", AttributeCategory.AGE);
        Attribute attBoy = new Attribute("boy", AttributeCategory.GENDER);
        Attribute attGirl = new Attribute("girl", AttributeCategory.GENDER);
        Attribute attApp1 = new Attribute("app1", AttributeCategory.APP);
        Attribute attApp2 = new Attribute("app2", AttributeCategory.APP);

        Item item = new ItemImpl("i[]", List.of());
        Item itemApp1 = new ItemImpl("i[app1]", List.of(
            new Clause(true, List.of(attApp1))
        ));
        Item itemApp2 = new ItemImpl("i[app2]", List.of(
            new Clause(true, List.of(attApp2))
        ));
        Item itemBoy = new ItemImpl("i[boy]", List.of(
            new Clause(true, List.of(attBoy)))
        );
        Item itemNotBoy = new ItemImpl("i[!boy]", List.of(
            new Clause(false, List.of(attBoy)))
        );
        Item itemBoyAndAge30 = new ItemImpl("i[boy][age30]", List.of(
            new Clause(true, List.of(attBoy)),
            new Clause(true, List.of(attAge30)))
        );
        Item itemNotBoyAndAge50 = new ItemImpl("i[!boy][age50]", List.of(
            new Clause(false, List.of(attBoy)),
            new Clause(true, List.of(attAge50)))
        );

        List<Item> candidateList = List.of(
            item, itemBoy, itemNotBoy, itemBoyAndAge30,
            itemNotBoyAndAge50, itemApp1, itemApp2
        );

        BooleanIndex booleanIndex = new BooleanIndex(candidateList);
        booleanIndex.show();

        Map<Assignment, List<Item>> assignment2ResultItemList = Map.of(
            new Assignment(), List.of(item, itemNotBoy),
            new Assignment(attApp1), List.of(item, itemNotBoy, itemApp1),
            new Assignment(attApp2), List.of(item, itemNotBoy, itemApp2),
            new Assignment(attGirl), List.of(item, itemNotBoy),
            new Assignment(attBoy), List.of(item, itemBoy),
            new Assignment(attAge30), List.of(item, itemNotBoy),
            new Assignment(attAge30, attBoy), List.of(item, itemBoy, itemBoyAndAge30),
            new Assignment(attAge30, attGirl), List.of(item, itemNotBoy),
            new Assignment(attAge50, attBoy), List.of(item, itemBoy),
            new Assignment(attAge50, attGirl), List.of(item, itemNotBoy, itemNotBoyAndAge50)
        );

        for (Map.Entry<Assignment, List<Item>> entry : assignment2ResultItemList.entrySet()) {
            Assignment assignment = entry.getKey();
            List<Item> expectItemList = sort(entry.getValue());
            List<Item> result = sort(booleanIndex.getItemsByAssignment(assignment));
            List<Item> loopResult = loopResult(assignment, candidateList);
            boolean success = Objects.equals(result, expectItemList) && Objects.equals(result, loopResult);
            if (success) {
                log.info("success [assignment {}, expect {}, actual {}]", assignment, expectItemList, result);
            } else {
                log.error("fail [assignment {}, expect {}, actual {}]", assignment, expectItemList, result);
            }
        }
    }

    private static List<Item> loopResult(Assignment assignment, List<Item> itemList) {
        Set<Attribute> attSet = new HashSet<>(assignment.getAttributeList());
        return sort(itemList.stream()
            .filter(item -> item.clauseList()
                .stream()
                .allMatch(clause -> clause.getAttributeList()
                    .stream()
                    .allMatch(att -> attSet.contains(att) ^ clause.isExclude())
                )
            )
            .collect(Collectors.toList())
        );
    }

    private static List<Item> sort(List<Item> items) {
        return items.stream().sorted(Comparator.comparing(Item::id)).collect(Collectors.toList());
    }

}
