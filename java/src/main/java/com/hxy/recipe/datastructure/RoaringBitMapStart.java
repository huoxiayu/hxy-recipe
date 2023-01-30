package com.hxy.recipe.datastructure;

import lombok.extern.slf4j.Slf4j;
import org.roaringbitmap.RoaringBitmap;

@Slf4j
public class RoaringBitMapStart {

    public static void main(String[] args) {
        RoaringBitmap bitMap123 = new RoaringBitmap();
        log.info("isEmpty {}", bitMap123.isEmpty());
        bitMap123.add(1, 2, 3);
        log.info("isEmpty {}", bitMap123.isEmpty());
        bitMap123.remove(1);
        bitMap123.remove(2);
        bitMap123.remove(2);
        log.info("isEmpty {}", bitMap123.isEmpty());

        RoaringBitmap roaringBitmap = RoaringBitmap.bitmapOf(1, 2, 9, 10);
        log.info("roaringBitmap: {}", toList(roaringBitmap));

        log.info("roaringBitmap.cardinality: {}", roaringBitmap.getLongCardinality());

        log.info("roaringBitmap.select(3): {}", roaringBitmap.select(3));

        log.info("roaringBitmap.rank(5): {}", roaringBitmap.rank(5));

        log.info("roaringBitmap.contains(5): {}", roaringBitmap.contains(5));

        log.info("roaringBitmap.contains(10): {}", roaringBitmap.contains(10));

        roaringBitmap.add(100);
        log.info("after add 100 roaringBitmap: {}", toList(roaringBitmap));
        log.info("roaringBitmap.cardinality: {}", roaringBitmap.getLongCardinality());

        RoaringBitmap otherRoaringBitmap = new RoaringBitmap();
        otherRoaringBitmap.add(100L, 110L);
        log.info("otherRoaringBitmap[100, 110]: {}", toList(otherRoaringBitmap));

        RoaringBitmap or = RoaringBitmap.or(roaringBitmap, otherRoaringBitmap);
        roaringBitmap.or(otherRoaringBitmap);
        log.info("or: {}", toList(or));
        log.info("roaringBitmap.or(otherRoaringBitmap): {}", toList(roaringBitmap));

        boolean equals = or.equals(roaringBitmap);
        log.info("equals: {}", equals);
    }

    private static String toList(RoaringBitmap roaringBitmap) {
        StringBuilder sb = new StringBuilder("[");
        for (int i : roaringBitmap) {
            sb.append(i).append(",");
        }
        int len = sb.length();
        sb.replace(len - 1, len, "]");
        return sb.toString();
    }

}
