package com.hxy.recipe.jdk.classloader;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

public class MemoryJavaFileManager extends ForwardingJavaFileManager {

    @Getter
    private final ConcurrentHashMap<String, byte[]> clazzNameMap = new ConcurrentHashMap<>();

    public MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                               String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new SimpleJavaFileObject(URI.create(className + ".class"), JavaFileObject.Kind.CLASS) {
                public OutputStream openOutputStream() {
                    return new FilterOutputStream(new ByteArrayOutputStream()) {
                        public void close() throws IOException {
                            out.close();
                            ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                            clazzNameMap.putIfAbsent(className, bos.toByteArray());
                        }
                    };
                }
            };
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

}
