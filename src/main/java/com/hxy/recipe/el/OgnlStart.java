package com.hxy.recipe.el;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.List;

@Slf4j
public class OgnlStart {

    @AllArgsConstructor
    @Getter
    private static class Basket {
        private String color;
        private List<Item> itemList;
    }

    @AllArgsConstructor
    @Getter
    private static class Item {
        private String property;
        private int value;
    }

    public static void main(String[] args) throws OgnlException {
        Basket basket = new Basket("red", List.of(new Item("p1", 1), new Item("p2", 2)));
        log.info("itemList[0].value => {}", Ognl.getValue(Ognl.parseExpression("itemList[0].value"), basket));
        log.info("itemList[1].getProperty() => {}", Ognl.getValue(Ognl.parseExpression("itemList[1].getProperty()"), basket));
    }

}
