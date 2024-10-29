package cn.bravedawn.exception;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 21:24
 */
public enum ExceptionEnum {

    REPLAY_PROTECT_PARAM_ERROR("0201", "防重放参数错误"),
    REPLAY_PROTECT_SIGN_OVERDUE_ERROR("0202", "签名已过期"),
    REPLAY_PROTECT_REPEAT_ERROR("0203", "请勿重复请求"),
    REPLAY_PROTECT_SIGN_VERIFY_ERROR("0204", "签名验证失败"),
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
