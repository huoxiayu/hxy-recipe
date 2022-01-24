package com.hxy.recipe.compute;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * -Xms20M
 * -Xmx20M
 * -XX:+TieredCompilation
 */
public class Loop {

    private static final int LEN = 64;
    private static final long TIME_IN_MILLIS = 100L;

    public static void main(String[] args) {
        try {
            System.out.println("enter loop");
            while (true) {
                run(TIME_IN_MILLIS);
                TimeUnit.SECONDS.sleep(2L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void run(long durationInMillis) {
        System.gc();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int sum = 0;
        int cnt = 0;
        Map<String, String> map = new HashMap<>(LEN << 1);
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start <= durationInMillis) {
            if (cnt == LEN) {
                map.clear();
            }
            map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

            int n = rand.nextInt(LEN) ^ rand.nextInt(LEN);
            sum += n;

            if (sum == Integer.MAX_VALUE) {
                System.out.println("amazing! sum: " + sum + " map.size(): " + map.size());
            }

            cnt++;
        }

        System.out.println("cnt: " + cnt);
        System.gc();
    }

}