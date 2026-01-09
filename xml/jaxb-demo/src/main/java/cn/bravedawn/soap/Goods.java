package cn.bravedawn.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 14:42
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Goods {

    private String id;
    private String price;
    private String name;
}
