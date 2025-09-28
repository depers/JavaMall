package cn.bravedawn.controlleradvice.exception;

import cn.bravedawn.controlleradvice.advice.EncryptControlUtil;
import cn.bravedawn.controlleradvice.config.RequestFilterConfig;
import cn.bravedawn.controlleradvice.dto.EncryptResponseDTO;
import cn.bravedawn.controlleradvice.dto.Result;
import cn.bravedawn.controlleradvice.util.SM2Util;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:22
 */

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class EncryptExceptionHandler {

    private final RequestFilterConfig requestFilterConfig;

    @ExceptionHandler(Throwable.class)
    public void handle(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        Result<?> failure = null;
        Object result = null;
        if (ex instanceof BusinessException) {
            log.error("出现本系统业务异常", ex);
            failure = Result.failure(((BusinessException) ex).getExceptionEnum());
        } else {
            log.error("出现其他异常", ex);
            failure = Result.failure(ExceptionEnum.SYSTEM_ERROR);
        }

        if (EncryptControlUtil.checkEncrypt()) {
            String jsonStr = JSONUtil.toJsonStr(failure);
            String encryptStr = SM2Util.encrypt(requestFilterConfig.getThirdPartyPublicKey(), jsonStr);
            EncryptResponseDTO responseDTO = new EncryptResponseDTO();
            responseDTO.setRespStr(encryptStr);
            log.info("异常响应报文打印: \n url:\t {}\t\n 响应的明文: \t{}\t\n 加密后的响应报文: \t{}\t\n", request.getRequestURI(), jsonStr, JSONUtil.toJsonStr(responseDTO));
            result = responseDTO;
        } else {
            result = failure;
            log.info("异常响应报文打印: \n url:\t {}\t\n 响应的明文: \t{}\t\n", request.getRequestURI(), JSONUtil.toJsonStr(result));
        }

        if (!response.isCommitted()) {
            try(OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                outputStream.flush();
            } catch (IOException e) {
                log.error("处理异常报文出现异常", e);
            }
        }
    }
}
