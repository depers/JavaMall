package cn.bravedawn.controlleradvice.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:24
 */
@Data
@Slf4j
public class BusinessException extends RuntimeException{


    private ExceptionEnum exceptionEnum;
    private String code;
    private String msg;


    public BusinessException(String msg) {
        this.msg = msg;
    }


    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(ExceptionEnum exceptionEnum, Object... messages) {
        this.code = exceptionEnum.getCode();
        this.msg = String.format(exceptionEnum.getMsg(), messages);
        this.exceptionEnum = exceptionEnum;
    }

    public BusinessException(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.msg = "[" + exceptionEnum.getCode() + "]" + exceptionEnum.getMsg();
        this.exceptionEnum = exceptionEnum;
    }
}
