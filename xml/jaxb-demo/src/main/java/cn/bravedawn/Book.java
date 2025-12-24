package cn.bravedawn;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2025-12-24 14:48
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Price")
    private String price;
}
