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

        Student student = Student.builder().id(10).name("hxy10").build();
        int insert = studentDao.insert(student);
        log.info("insert: {}", insert);

        List<Student> studentList = studentDao.getAllStudent();
        log.info("studentList is: {}", studentList);
    }
}
