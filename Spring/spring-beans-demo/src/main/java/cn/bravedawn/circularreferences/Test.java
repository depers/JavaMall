package cn.bravedawn.circularreferences;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.beans.Beans;

/**
 * @Author : depers
 * @Date : Created in 2025-12-05 16:52
 */
public class Test {


    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("cn.bravedawn.circularreferences");
        A a = ctx.getBean("a", A.class);
        a.print();
    }
}
