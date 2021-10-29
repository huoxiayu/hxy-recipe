package com.hxy.recipe.dictionary;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class DictionaryTree<T> {

    private final Entry<T> root = new Entry<>();

    public T putIfAbsent(CharSequence charSequence, Supplier<T> supplier) {
        Entry<T> point = getEntry(charSequence);

        T v = point.getV();
        if (v == null) {
            v = supplier.get();
            point.setV(v);
        }

        return v;
    }

    public T get(CharSequence charSequence) {
        Entry<T> point = getEntry(charSequence);
        return point.getV();
    }

    private Entry<T> getEntry(CharSequence charSequence) {
        int len = charSequence.length();
        Entry<T> point = root;
        for (int i = 0; i < len; i++) {
            char ch = charSequence.charAt(i);
            point = point.getNext(ch);
            point.setCh(ch);
        }

        return point;
    }

    public static void main(String[] args) {
        AtomicInteger inc = new AtomicInteger();
        DictionaryTree<Integer> dt = new DictionaryTree<>();

        System.out.println(dt.putIfAbsent("111", inc::incrementAndGet));
        System.out.println(dt.putIfAbsent("112", inc::incrementAndGet));
        System.out.println(dt.putIfAbsent("123", inc::incrementAndGet));
        System.out.println(dt.putIfAbsent("1234", inc::incrementAndGet));

        // debug
        System.out.println(dt.toString());
    }

}
