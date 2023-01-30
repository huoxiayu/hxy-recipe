package com.hxy.recipe.dictionary;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
class Entry<T> {

    private static final int DIGIT_KINDS = 10;
    private static final int LETTER_KINDS = 26;
    private static final int KINDS = DIGIT_KINDS + LETTER_KINDS;

    private char ch;
    private List<Entry<T>> next;
    private T v;

    Entry<T> getNext(char ch) {
        int idx = idx(ch);
        if (next == null) {
            next = newEntryList();
        }
        return next.get(idx);
    }

    private int idx(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return ch - 'a' + DIGIT_KINDS;
        }

        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }

        throw new IllegalStateException("unsupported character");
    }

    private List<Entry<T>> newEntryList() {
        List<Entry<T>> entryList = new ArrayList<>(KINDS);
        for (int i = 0; i < KINDS; i++) {
            entryList.add(new Entry<>());
        }

        return entryList;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "ch=" + ch +
                ", v=" + v +
                '}';
    }

}
