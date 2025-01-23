package cn.bravedawn.slf4j;


import lombok.extern.slf4j.Slf4j;

/**
 * 演示@Slf4j注解topic属性的使用
 */

@Slf4j(topic = "演示topic属性的作用")
public class TopicExample {

    public static void main(String[] args) {
        log.info("hello topic");
    }

}
