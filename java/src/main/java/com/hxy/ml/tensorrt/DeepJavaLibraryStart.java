package com.hxy.ml.tensorrt;

import ai.djl.engine.Engine;
import lombok.SneakyThrows;

public class DeepJavaLibraryStart {

    @SneakyThrows
    public static void main(String[] args) {
        Engine engine = Engine.getEngine("TensorRT");
        String version = engine.getVersion();
        System.out.println(version);
    }

}
