package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class Reflection {

    @Setter
    @Data
    private static class Bean {
        private String id;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        log.info("{}", Boolean.TYPE == Boolean.class);

        Bean bean = new Bean();
        Field f = Bean.class.getDeclaredField("id");
        f.setAccessible(true);

        long cost1 = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            try {
                f.set(bean, "111");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, 1000_0000));
        log.info("cost: {}", cost1);

        long cost2 = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            bean.setId("111");
        }, 1000_0000));
        log.info("cost: {}", cost2);
    }

}
