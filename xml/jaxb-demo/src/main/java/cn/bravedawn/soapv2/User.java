package cn.bravedawn.soapv2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2026-01-13 16:07
 */

@Data
@XmlRootElement(name = "svcBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @XmlElement(name = "Username")
    private String username;

    @XmlElement(name = "Age")
    private String age;
}
