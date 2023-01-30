package com.hxy.recipe.jdk.classloader;

import com.hxy.recipe.clazz.ClazzDefineUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

@Slf4j
public class ClassLoaderStart {

    public static void main(String[] args) throws Exception {
        StringJavaFile sourceFile = new StringJavaFile(
            ClazzDefineUtil.clazzPath(),
            ClazzDefineUtil.clazzContent("self-define-clazz")
        );
        log.info("sourceFile.getClazzName(): {}", sourceFile.getClazzName());
        log.info("sourceFile.getClazzContent(): {}", sourceFile.getClazzContent());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        MemoryJavaFileManager memoryJavaFileManager = new MemoryJavaFileManager(
            compiler.getStandardFileManager(null, null, null)
        );

        JavaCompiler.CompilationTask task = compiler.getTask(
            null,
            memoryJavaFileManager,
            null,
            null,
            null,
            List.of(sourceFile)
        );

        boolean result = task.call();
        log.info("compile {}", result ? "success" : "fail");
        if (result) {
            log.info("load classes: {}", memoryJavaFileManager.getClazzNameMap().keySet());

            List<Object> list = new ArrayList<>(2);
            // true
            // MemoryClassLoader memoryClassLoader = new MemoryClassLoader(memoryJavaFileManager.getClazzNameMap());
            for (int i = 0; i < 2; i++) {
                // false
                MemoryClassLoader memoryClassLoader = new MemoryClassLoader(memoryJavaFileManager.getClazzNameMap());
                Class<?> clazz = Class.forName(sourceFile.getClazzName(), true, memoryClassLoader);
                log.info("clazz: {}", clazz.getName());

                Constructor constructor = clazz.getConstructor();
                Object instance = constructor.newInstance();
                list.add(instance);

                Method method = clazz.getMethod("print");
                method.invoke(instance);
            }

            Object obi = list.get(0);
            Object obj = list.get(1);
            log.info("obi {}, obj {}", obi.getClass(), obj.getClass());
            log.info("obj.class == obi.class {}", obi.getClass().equals(obj.getClass()));
        }
    }

}
