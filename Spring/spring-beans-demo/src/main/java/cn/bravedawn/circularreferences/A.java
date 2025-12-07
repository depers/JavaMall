package cn.bravedawn.circularreferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author : depers
 * @Date : Created in 2025-12-05 16:50
 */


@Component
public class A {

    @Autowired
    private B b;

    public void print() {
        System.out.println("我是A");
    }

}
