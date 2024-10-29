package cn.bravedawn.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 21:23
 */
@Slf4j
public class BusinessException extends RuntimeException {

    private ExceptionEnum exceptionEnum;
    private String code;
    private String message;


    public BusinessException(ExceptionEnum exceptionEnum, Object... messages) {
        this.exceptionEnum = exceptionEnum;
        this.code = exceptionEnum.getCode();
        this.message = String.format(exceptionEnum.getMsg(), messages);
    }


    public BusinessException(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = "[" + exceptionEnum.getCode() + "]" + exceptionEnum.getMsg();
        this.exceptionEnum = exceptionEnum;
    }


    public BusinessException(ExceptionEnum exceptionEnum, Throwable e) {
        log.error("出现其他异常", e);
        this.message = exceptionEnum.getMsg();
        this.code = exceptionEnum.getCode();
        this.exceptionEnum = exceptionEnum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
