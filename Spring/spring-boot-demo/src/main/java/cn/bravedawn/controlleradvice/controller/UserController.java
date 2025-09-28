package cn.bravedawn.controlleradvice.controller;

import cn.bravedawn.controlleradvice.dto.Result;
import cn.bravedawn.controlleradvice.dto.user.UserReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:23
 */
@Slf4j
@RestController
@RequestMapping("/filter")
public class UserController {


    @PostMapping("addUser")
    public Result<?> addUser(@RequestBody UserReqDTO userReqDTO) {
        log.info("添加用户：{}", userReqDTO);
        return Result.success(null);
    }

    @PostMapping("addUserV2")
    public Result<?> addUserV2(@RequestBody UserReqDTO userReqDTO) {
        log.info("添加用户：{}", userReqDTO);
        int i = 1 / 0;
        return Result.success(null);
    }


}
