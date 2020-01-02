package com.hxy.recipe.mybatis.dao;

import com.hxy.recipe.mybatis.common.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentDao {

    @Select("select * from student")
    List<Student> getAllStudent();

    @Insert("insert into student (name, hobby, country, offline) values " +
        "(#{name}, #{hobby}, #{country}, #{offline})")
    int insert(Student student);

}
