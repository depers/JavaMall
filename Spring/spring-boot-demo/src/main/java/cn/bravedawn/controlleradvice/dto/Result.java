package cn.bravedawn.controlleradvice.dto;

import cn.bravedawn.controlleradvice.exception.ExceptionEnum;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:33
 */
@Data
public class Result<T> {

    private static final String SUCCESS_CODE = "0000";
    private static final String SUCCESS_MSG = "交易成功";

    private String code;
    private String msg;
    private T data;


    public static <R> Result<R> success(R data) {
        Result<R> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMsg(SUCCESS_MSG);
        result.setData(data);
        return result;
    }


    public static Result<?> failure(ExceptionEnum exceptionEnum) {
        Result<?> result = new Result<>();
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());
        return result;
    }
}
