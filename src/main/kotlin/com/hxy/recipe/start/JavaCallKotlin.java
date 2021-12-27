package com.hxy.recipe.start;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class JavaCallKotlin {

    public static void main(String[] args) {
        System.out.println(KotlinCallJava.class);

        KotlinObject.kotlinObjectPrint();

        KotlinCallJava.kotlinStaticPrint();

        new KotlinCallJava().kotlinInstancePrint();

        Function1<String, Unit> echo = KotlinCallJavaMain.getEcho();
        echo.invoke("echo");

        KotlinCallJavaMain.main();
    }

    public static void javaStaticPrint() {
        System.out.println("javaStaticPrint");
    }

    public void javaInstancePrint() {
        System.out.println("javaInstancePrint");
    }

}
