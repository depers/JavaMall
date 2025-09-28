package cn.bravedawn.common;

import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2025-09-25 18:19
 */
@Data
public class CommonRespDTO<T> {
    private String code;
    private String msg;
    private T data;
}
