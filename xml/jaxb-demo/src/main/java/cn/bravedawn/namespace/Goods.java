package cn.bravedawn.namespace;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 14:42
 */
@Data
@XmlRootElement(name = "goods")
@XmlAccessorType(XmlAccessType.FIELD)
public class Goods {

    @XmlElement(name = "id", namespace = "http://example.com/schema")
    private String id;
    @XmlElement(name = "price", namespace = "http://example.com/schema")
    private String price;
    @XmlElement(name = "name", namespace = "http://example.com/schema")
    private String name;
}
