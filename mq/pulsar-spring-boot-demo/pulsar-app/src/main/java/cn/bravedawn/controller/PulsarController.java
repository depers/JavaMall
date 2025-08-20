package cn.bravedawn.controller;

import cn.bravedawn.contant.PriorityEnum;
import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.core.PulsarTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        pulsarMessage.setTopicPrefix("sms");
        Map<String, Object> properties = new HashMap<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("jack");
        userDTO.setAge(18);
        userDTO.setBrithday(new Date());
        properties.put("user", userDTO);
        pulsarMessage.setProperties(properties);
        pulsarMessage.setPriorityEnum(PriorityEnum.NORMAL);

        pulsarTemplate.sendMessage(pulsarMessage);
        return "success";
    }



    @GetMapping("/sendPriorityMsg")
    public String sendPriorityMsg() {
        PulsarMessage pulsarMessage = new PulsarMessage();
        pulsarMessage.setTopicPrefix("sms");
        Map<String, Object> properties = new HashMap<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("tom");
        userDTO.setAge(19);
        userDTO.setBrithday(new Date());
        properties.put("user", userDTO);
        pulsarMessage.setProperties(properties);
        pulsarMessage.setPriorityEnum(PriorityEnum.HIGH);

        pulsarTemplate.sendMessage(pulsarMessage);
        return "success";
    }

}
