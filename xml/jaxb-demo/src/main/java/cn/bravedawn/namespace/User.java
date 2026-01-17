package cn.bravedawn.namespace;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 11:15
 */
@Data
@XmlRootElement(name = "SvcBody", namespace = "http://example.com/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @XmlElement(name = "id", namespace = "http://example.com/schema")
    private Long id;

    @XmlElement(name = "name", namespace = "http://example.com/schema")
    private String name;

//    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<Goods> goodsList;

    public User() {}

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
