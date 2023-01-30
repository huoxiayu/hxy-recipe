package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.roaringbitmap.RoaringBitmap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestCaseUtil {

    public static RoaringBitmap roaringBitmap(int from, int to) {
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        for (int i = from; i < to; i++) {
            roaringBitmap.add(i);
        }
        return roaringBitmap;
    }

}
