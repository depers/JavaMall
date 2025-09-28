package cn.bravedawn.controlleradvice.advice;

import cn.bravedawn.controlleradvice.config.RequestFilterConfig;
import cn.bravedawn.controlleradvice.dto.EncryptRequestDTO;
import cn.bravedawn.controlleradvice.util.SM2Util;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 13:58
 */

@Slf4j
@ControllerAdvice
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {

    @Autowired
    private RequestFilterConfig requestFilterConfig;

    /**
     * 是否支持处理该请求
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }


    /**
     * 在请求体被解析为对象之前调用。
     * 可以对原始请求体进行修改或是参数校验。
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }


    /**
     * 在请求体被解析为对象之后调用。
     * 可以对解析后的对象进一步进行处理。
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        ContentCachingRequestWrapper contentCachingRequestWrapper = (ContentCachingRequestWrapper) request;
        String requestUri = request.getRequestURI();
        String requestStr = new String(contentCachingRequestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        boolean checkEncrypt = EncryptControlUtil.checkEncrypt();
        if (checkEncrypt) {
            EncryptRequestDTO encryptRequestDTO = JSONUtil.toBean(requestStr, EncryptRequestDTO.class);
            String decryptStr = SM2Util.decrypt(requestFilterConfig.getBankPrivateKey(), encryptRequestDTO.getReqStr());
            Object obj = JSONUtil.toBean(decryptStr, targetType, true);
            log.info("请求报文打印: \n url:\t {}\t\n 请求的密文: \t{}\t\n 解密后的请求报文: \t{}\t\n", requestUri, requestStr, decryptStr);
            return obj;
        } else {
            log.info("请求报文打印: \n url:\t {}\t\n 请求的明文: \t{}\t\n", requestUri, requestStr);
            return body;
        }


    }

    /**
     * 当请求体为空时调用。
     * 可以处理空请求体的逻辑
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
