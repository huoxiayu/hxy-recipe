package com.hxy.recipe.spring.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class SpringStart {

    public static void main(String[] args) {
        String path = "spring.xml";

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
        context.start();

        Class[] classes = {AutowiredBean.class, JavaConfigBean.class, XmlBean.class};
        for (Class clazz : classes) {
            log.info(clazz.toString());
        }
    }

}
