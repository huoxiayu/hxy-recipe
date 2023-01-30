package com.hxy.recipe.jdk.classloader;

import java.util.Map;

public class MemoryClassLoader extends ClassLoader {

    private final Map<String, byte[]> clazzNameMap;

    public MemoryClassLoader(Map<String, byte[]> clazzNameMap) {
        this.clazzNameMap = clazzNameMap;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = clazzNameMap.get(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }

        return defineClass(name, bytes, 0, bytes.length);
    }

}
