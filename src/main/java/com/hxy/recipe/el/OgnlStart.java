package com.hxy.recipe.el;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;

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

    public static void main(String[] args) throws Exception {
        Basket basket = new Basket("red", List.of(
            new Item("p1", 1),
            new Item("p2", 2))
        );
        String expression1 = "itemList[0].value";
        log.info("{} => {}", expression1, Ognl.getValue(Ognl.parseExpression(expression1), basket));

        String expression2 = "itemList[1].getProperty()";
        log.info("{} => {}", expression2, Ognl.getValue(Ognl.parseExpression(expression2), basket));

        String expression3 = "@java.lang.Math@random()";
        log.info("{} => {}", expression3, Ognl.getValue(Ognl.parseExpression(expression3), null));

        String expression4 = "@java.lang.Math@random() < 0.5";
        log.info("{} => {}", expression4, Ognl.getValue(Ognl.parseExpression(expression4), null));

        String expression5 = "@java.util.concurrent.ThreadLocalRandom@current().nextDouble() > 0.5";
        log.info("{} => {}", expression5, Ognl.getValue(Ognl.parseExpression(expression5), null));

        long parseEveryTime = BenchmarkUtil.singleRun(
            RunnableUtil.loopExceptionRunnable(
                () -> Ognl.getValue(Ognl.parseExpression(expression1), basket)
            )
        );
        log.info("normal cost {} millis", parseEveryTime);

        Object preparse = Ognl.parseExpression(expression1);
        long parsedCost = BenchmarkUtil.singleRun(
            RunnableUtil.loopExceptionRunnable(
                () -> Ognl.getValue(preparse, basket)
            )
        );
        log.info("preparse cost {} millis", parsedCost);

        long oriCost = BenchmarkUtil.singleRun(
            RunnableUtil.loopExceptionRunnable(
                (() -> basket.getItemList().get(0).getValue())
            )
        );
        log.info("ori cost {} millis", oriCost);
    }

}
