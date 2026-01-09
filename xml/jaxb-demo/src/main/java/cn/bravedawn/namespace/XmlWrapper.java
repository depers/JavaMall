package cn.bravedawn.namespace;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 15:24
 */

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlWrapper<T> {

    @XmlAnyElement(lax = true)
    private T value;

    public XmlWrapper() {}

    public XmlWrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

