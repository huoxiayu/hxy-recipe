package com.hxy.recipe.offheap.io;

import com.hxy.recipe.util.JsonUtil;
import jdk.internal.misc.Unsafe;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * IDEA vm options add below line:
 * --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
 */
@Slf4j
public class ObjectLayout {

    public static void main(String[] args) {
        Bean bean = new Bean();
        bean.setOuterInt(100);
        bean.setOuterLong(200L);
        bean.setOuterBool(true);
        bean.setOuterDouble(300D);
        bean.setOuterString("outer-string");
        InnerBean innerBean = new InnerBean();
        bean.setInnerBean(innerBean);
        innerBean.setInnerInt(10000);
        innerBean.setInnerLong(20000L);
        innerBean.setInnerBool(true);
        innerBean.setInnerDouble(30000D);
        innerBean.setInnerString("inner-string");
        log.info("bean -> {}", JsonUtil.toJson(bean));
    }

}
