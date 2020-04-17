package com.hxy.recipe.el;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.List;

@Slf4j
public class OgnlStart {

    private static final Basket BASKET = new Basket("red", List.of(
        new Item("p1", 1),
        new Item("p2", 2))
    );

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
        run();

        performance("itemList[0].value", BASKET);

        performance("@java.lang.Math@random()", null);
    }

    /**
     * @see <a href="https://commons.apache.org/proper/commons-ognl/language-guide.html">ognl</a>
     */
    private static void run() throws OgnlException {
        runOgnlExpression("itemList[0].value", BASKET);

        runOgnlExpression("itemList[1].getProperty()", BASKET);

        runOgnlExpression("itemList[1].getProperty().(#this.length(), 234 + 432)", BASKET);

        runOgnlExpression("itemList[1].getProperty().(234 + 432, #this.length())", BASKET);

        runOgnlExpression("itemList[1].getProperty() in { 'p2' }", BASKET);

        // in list
        runOgnlExpression("null in { '' }", null);

        runOgnlExpression("null in { null }", null);

        // method, use @ to reference static class and static method
        runOgnlExpression("@java.lang.Math@random()", null);

        runOgnlExpression("@java.lang.Math@random() < 0.5", null);

        runOgnlExpression("@java.util.concurrent.ThreadLocalRandom@current().nextDouble() > 0.5", null);

        runOgnlExpression("@System@out.println('System.out.println call')", null);

        // this
        runOgnlExpression("#this.size().(#this >= 3 ? 2 * #this : 10 + #this)", List.of(1, 2, 3));

        runOgnlExpression("#this.size().(#this >= 3 ? 2 * #this : 10 + #this)", List.of(1));

        // projection
        runOgnlExpression("#this.{ #this.property }", List.of(
            new Item("p1", 1),
            new Item("p2", 2))
        );

        // selecting
        // filter
        runOgnlExpression("#this.{? #this instanceof String }", List.of(1, true, "string"));

        // first
        runOgnlExpression("#this.{^ #this instanceof String }", List.of("first", "last"));

        // last
        runOgnlExpression("#this.{$ #this instanceof String }", List.of("first", "last"));

        // use variable
        runOgnlExpression("#var='string'", null);

        // single value
        runOgnlExpression("#var1=11, #var2=22, #var1 + #var2", null);

        // return list
        runOgnlExpression("#var1=11, #var2=22, {#var1 + #var2}", null);

        // constructor
        runOgnlExpression("#list=new java.util.ArrayList(), #list.add(1), #list.add(2), #list", null);
    }

    private static void runOgnlExpression(String expression, Object root) throws OgnlException {
        log.info("{} => {}", expression, Ognl.getValue(Ognl.parseExpression(expression), root));
    }

    // pre-parse are several hundred times faster
    private static void performance(String expression, Object root) throws OgnlException {
        long parseEveryTime = BenchmarkUtil.singleRun(
            RunnableUtil.loopExceptionRunnable(
                () -> Ognl.getValue(Ognl.parseExpression(expression), root)
            )
        );
        log.info("normal cost {} millis", parseEveryTime);

        Object preparse = Ognl.parseExpression(expression);
        long parsedCost = BenchmarkUtil.singleRun(
            RunnableUtil.loopExceptionRunnable(
                () -> Ognl.getValue(preparse, root)
            )
        );
        log.info("preparse cost {} millis", parsedCost);
    }

}
