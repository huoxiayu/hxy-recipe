package com.hxy.recipe.springbootext;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SystemPropertyFetcher implements DynamicPropertyFetcher {

    @Override
    public Map<String, String> getAllConfig() {
        Properties properties = System.getProperties();
        Map<String, String> kvMap = new HashMap<>(properties.size());
        properties.forEach((k, v) -> kvMap.put(String.valueOf(k), String.valueOf(v)));
        return kvMap;
    }

}
