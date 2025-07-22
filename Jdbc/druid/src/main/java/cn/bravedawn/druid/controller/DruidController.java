package cn.bravedawn.druid.controller;

import cn.bravedawn.druid.service.DruidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : depers
 * @program : druid
 * @description: 测试controller
 * @date : Created in 2021/7/31 19:18
 */

@RestController
@RequestMapping("/test")
public class DruidController {

    @Autowired
    private DruidService druidService;


    @GetMapping("/selectKey")
    public String selectKey(){
        return String.valueOf(druidService.selectKey());
    }

    @GetMapping("/add")
    public String insert() {
        return String.valueOf(druidService.add());
    }


}
