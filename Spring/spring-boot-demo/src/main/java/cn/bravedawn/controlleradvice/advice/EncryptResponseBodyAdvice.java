package cn.bravedawn.controlleradvice.advice;

import cn.bravedawn.controlleradvice.config.RequestFilterConfig;
import cn.bravedawn.controlleradvice.dto.EncryptResponseDTO;
import cn.bravedawn.controlleradvice.util.SM2Util;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:01
 */

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice {

    private final RequestFilterConfig requestFilterConfig;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest req, ServerHttpResponse response) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String requestUri = request.getRequestURI();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        boolean checkEncrypt = EncryptControlUtil.checkEncrypt();
        if (checkEncrypt) {
            String jsonStr = JSONUtil.toJsonStr(body);
            String encryptStr = SM2Util.encrypt(requestFilterConfig.getThirdPartyPublicKey(), jsonStr);
            EncryptResponseDTO responseDTO = new EncryptResponseDTO();
            responseDTO.setRespStr(encryptStr);
            log.info("响应报文打印: \n url:\t {}\t\n 响应的明文: \t{}\t\n 加密后的响应报文: \t{}\t\n", requestUri, jsonStr, JSONUtil.toJsonStr(responseDTO));
            return responseDTO;
        } else {
            log.info("响应报文打印: \n url:\t {}\t\n 响应的明文: \t{}\t\n", requestUri, JSONUtil.toJsonStr(body));
            return body;
        }

    }
}
