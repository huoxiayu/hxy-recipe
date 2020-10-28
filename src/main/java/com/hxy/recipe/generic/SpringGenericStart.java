package com.hxy.recipe.generic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SpringGenericStart {

    public static void main(String[] args) {
        List<String> listString = new ArrayList<>();
        ResolvableType typeOfListString = ResolvableType.forInstance(listString);
        log.info("typeOfListString: {}", typeOfListString);

        List<List<String>> listListString = new ArrayList<>();
        ResolvableType typeOfListListString = ResolvableType.forInstance(listListString);
        log.info("typeOfListListString: {}", typeOfListListString);

        Map<Boolean, List<Map<String, Integer>>> map = new HashMap<>();
        ResolvableType typeOfCompositeMap = ResolvableType.forInstance(map);
        log.info("typeOfCompositeMap: {}", typeOfCompositeMap);

        ResolvableType typeOfClassField1 = ResolvableType.forField(ReflectionUtils.findField(GenericPOJO.class, "list"));
        log.info("typeOfClassField1: {}", typeOfClassField1);

        ResolvableType typeOfClassField2 = ResolvableType.forField(ReflectionUtils.findField(GenericPOJO.class, "map"));
        log.info("typeOfClassField2: {}", typeOfClassField2);

        Class<?> resolve = typeOfClassField2.getGeneric(0).resolve();
        log.info("resolve: {}", resolve);
    }

}
