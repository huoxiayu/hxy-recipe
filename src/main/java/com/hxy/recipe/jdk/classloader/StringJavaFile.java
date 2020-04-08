package com.hxy.recipe.jdk.classloader;

import com.hxy.recipe.clazz.ClazzDefineUtil;
import lombok.Getter;

import javax.tools.SimpleJavaFileObject;

@Getter
public class StringJavaFile extends SimpleJavaFileObject {

    private final String clazzName;
    private final String clazzContent;

    public StringJavaFile(String classPath, String clazzContent) {
        super(ClazzDefineUtil.clazzUrl(classPath), Kind.SOURCE);
        this.clazzName = classPath.replace("/", ".");
        this.clazzContent = clazzContent;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return clazzContent;
    }

}
