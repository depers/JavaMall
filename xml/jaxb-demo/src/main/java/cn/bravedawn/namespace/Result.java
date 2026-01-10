package cn.bravedawn.namespace;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 21:18
 */
@Data
@XmlRootElement(name = "svcBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result<T> {

    private int code;
    private String msg;

    @XmlAnyElement(lax = true)
    private T data;

    public Result() {}

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}

