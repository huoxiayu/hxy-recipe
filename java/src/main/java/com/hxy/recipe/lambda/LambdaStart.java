package com.hxy.recipe.lambda;

/**
 * VM options add:
 * -Djdk.internal.lambda.dumpProxyClasses=/Users/tmp
 */
public class LambdaStart {

    public static void main(String[] args) {
        // dump class to current path
        System.setProperty("jdk.internal.lambda.dumpProxyClasses", ".");

        /**
         * lambda will generate extra static method 'private static void lambda$main$0()'
         * public class com.hxy.recipe.lambda.LambdaStart {
         *   public com.hxy.recipe.lambda.LambdaStart();
         *   public static void main(java.lang.String[]);
         *   private static void print();
         *   private static void run(java.lang.Runnable);
         *   private static void lambda$main$0();
         * }
         */
        run(() -> print());

        /**
         * method reference will not
         * public class com.hxy.recipe.lambda.LambdaStart {
         *   public com.hxy.recipe.lambda.LambdaStart();
         *   public static void main(java.lang.String[]);
         *   private static void print();
         *   private static void run(java.lang.Runnable);
         * }
         */
        // run(LambdaStart::print);
    }

    private static void print() {
        System.out.println("lambda");
    }

    private static void run(Runnable runnable) {
        runnable.run();
    }

}
