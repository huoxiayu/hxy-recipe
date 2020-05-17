package com.hxy.algo.bool.index;

import java.util.List;
import java.util.stream.Collectors;

public class IndexStart {

    public static void main(String[] args) {
        Attribute attAge30 = new Attribute("30", AttributeCategory.AGE);
        Attribute attAge50 = new Attribute("50", AttributeCategory.AGE);
        Attribute attBoy = new Attribute("boy", AttributeCategory.GENDER);
        Attribute attGirl = new Attribute("girl", AttributeCategory.GENDER);

        Item item = new ItemImpl("i[]", List.of());
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

        List<Item> candidateList = List.of(item, itemBoy, itemNotBoy, itemBoyAndAge30, itemNotBoyAndAge50);
        System.out.println("candidate: " + candidateList.stream().map(Item::id).collect(Collectors.joining(",")));

        BooleanIndex booleanIndex = new BooleanIndex(candidateList);
        booleanIndex.show();

        List<Assignment> assignmentList = List.of(
            new Assignment(),
            new Assignment(attBoy),
            new Assignment(attGirl),
            new Assignment(attAge30),
            new Assignment(attAge30, attBoy),
            new Assignment(attAge30, attGirl),
            new Assignment(attAge50, attBoy),
            new Assignment(attAge50, attGirl)
        );

        List<Runnable> caseList = assignmentList.stream()
            .map(assignment -> (Runnable) () -> {
                List<Item> itemList = booleanIndex.getItemsByAssignment(assignment);
                System.out.println(assignment + " => " + itemList);
                System.out.println("<-------------------->");
            })
            .collect(Collectors.toList());
        caseList.forEach(Runnable::run);
    }
}
