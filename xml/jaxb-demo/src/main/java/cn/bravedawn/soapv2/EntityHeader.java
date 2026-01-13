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
@XmlRootElement(name = "EntityHeader")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityHeader {


    @XmlElement(name = "SysId")
    private String sysId;
}
