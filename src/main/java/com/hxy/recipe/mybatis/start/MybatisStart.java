package com.hxy.recipe.mybatis.start;

import com.hxy.recipe.RecipeApplication;
import com.hxy.recipe.mybatis.common.Student;
import com.hxy.recipe.mybatis.dao.StudentDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@Slf4j
public class MybatisStart {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RecipeApplication.class, args);
        StudentDao studentDao = context.getBean(StudentDao.class);

        Student onlineStudent = Student.builder().name("x").hobby("ball").country("CN").offline(false).build();
        int insert = studentDao.insert(onlineStudent);
        log.info("insert: {}", insert);

        Student offlineStudent = Student.builder().name("y").hobby("ping-pang").country("CN").offline(true).build();
        insert = studentDao.insert(offlineStudent);
        log.info("insert: {}", insert);

        List<Student> studentList = studentDao.getAllStudent();
        log.info("studentList is: {}", studentList);
    }
}
