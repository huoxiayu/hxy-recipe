package com.hxy.recipe.rpc.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Slf4j
public class KryoStart {

    private static final String TARGET_PATH = "kryo.data";

    public static void main(String[] args) throws FileNotFoundException {
        Kryo kryo = new Kryo();
        kryo.register(Student.class);

        Student student = new Student();
        student.setName("Hello Kryo!");
        student.setAge(3);

        Output output = new Output(new FileOutputStream(TARGET_PATH));
        kryo.writeObject(output, student);
        output.close();

        Input input = new Input(new FileInputStream(TARGET_PATH));
        Student readStudent = kryo.readObject(input, Student.class);
        log.info("read student: {}", readStudent);
        input.close();
    }

    @ToString
    @Data
    private static class Student {
        private String name;
        private int age;
    }

}
