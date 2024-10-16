package cn.bravedawn.springsecurity.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {


    /**
     * 这块逻辑在AccessDeniedException异常时，程序抛出当访问接口没有权限时返回
     */

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("当前接口没有访问权限", accessDeniedException);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("禁止访问");
        response.getWriter().flush();
    }
}
