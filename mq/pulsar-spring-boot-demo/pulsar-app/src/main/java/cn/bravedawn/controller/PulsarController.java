package cn.bravedawn.controller;

import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.core.PulsarTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-22 22:40
 */

@RestController
public class PulsarController {

    @Autowired
    private PulsarTemplate pulsarTemplate;

    @GetMapping("/sendMsg")
    public String sendMsg() {
        PulsarMessage pulsarMessage = new PulsarMessage();
        pulsarMessage.setTopicPrefix();
        pulsarTemplate.sendMessage();
    }

}
