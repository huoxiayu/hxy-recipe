package com.hxy.recipe.classloader;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ScannerClassStart {

    public static void reflectionsTest() {
        Set<URL> allPackagePrefixList = Arrays.stream(Package.getPackages()).map(Package::getName)
                .map(s -> s.split("\\.")[0])
                .distinct()
                .map(ClasspathHelper::forPackage)
                .map(HashSet::new)
                .reduce((c1, c2) -> {
                    c1.addAll(c2);
                    return c1;
                }).orElseThrow();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(allPackagePrefixList)
                //.forPackages("java")
                .addScanners(new SubTypesScanner())
                .addScanners(new FieldAnnotationsScanner())
                .addScanners(new MethodAnnotationsScanner())
                .addScanners(new MethodParameterScanner())
        );
        Set<Class<? extends InputStream>> set = reflections.getSubTypesOf(InputStream.class);
        System.out.println("getSubTypesOf: " + set.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }

    public static void main(String[] args) {
        reflectionsTest();
    }

}

