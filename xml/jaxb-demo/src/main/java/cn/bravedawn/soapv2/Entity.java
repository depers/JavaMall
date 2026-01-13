package cn.bravedawn.soapv2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2026-01-13 16:06
 */
@Data
@XmlRootElement(name = "Envelop")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entity<T> {

    @XmlElement(name = "Body")
    private Body<T> body;

    @XmlElement(name = "EntityHeader")
    public EntityHeader entityHeader;

}
