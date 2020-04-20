package com.hxy.algo.indexbooleanexpressionstart;

import com.hxy.algo.indexbooleanexpression.Assignment;
import com.hxy.algo.indexbooleanexpression.Attribute;
import com.hxy.algo.indexbooleanexpression.AttributeCategory;
import com.hxy.algo.indexbooleanexpression.BooleanIndexing;
import com.hxy.algo.indexbooleanexpression.Clause;
import com.hxy.algo.indexbooleanexpression.Item;
import com.hxy.algo.indexbooleanexpression.ItemImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IndexingBooleanExpressionStart {

    public static void main(String[] args) {
        Attribute age30 = new Attribute("30", AttributeCategory.AGE);
        Attribute age50 = new Attribute("50", AttributeCategory.AGE);
        Attribute genderBoy = new Attribute("boy", AttributeCategory.GENDER);

        Item itemBoy = new ItemImpl("item-boy", List.of(new Clause(true, List.of(genderBoy))));
        Item itemNotBoy = new ItemImpl("item-not-boy", List.of(new Clause(false, List.of(genderBoy))));
        Item itemBoyAge30 = new ItemImpl("item-boy-age30", List.of(new Clause(true, List.of(genderBoy, age30))));

        List<Item> candidateList = List.of(itemBoy, itemNotBoy, itemBoyAge30);
        log.info("candidate: {}", candidateList);

        BooleanIndexing booleanIndexing = new BooleanIndexing(candidateList);
        booleanIndexing.getItemsByAssignment(new Assignment(genderBoy));
        booleanIndexing.getItemsByAssignment(new Assignment(age30));
        booleanIndexing.getItemsByAssignment(new Assignment(age30, genderBoy));
        booleanIndexing.getItemsByAssignment(new Assignment(age50, genderBoy));
    }

}
