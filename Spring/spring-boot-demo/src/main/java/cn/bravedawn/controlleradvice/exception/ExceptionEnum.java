package cn.bravedawn.controlleradvice.exception;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:25
 */
public enum ExceptionEnum {

    // 业务异常
    REQUEST_PARAM_ERROR("0001", "请求参数错误"),


    // 系统异常
    SYSTEM_ERROR("2001", "系统异常，请稍后再试")

    ;


    private String code;
    private String msg;

    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
