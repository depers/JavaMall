package cn.bravedawn.soapv2;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2026-01-13 16:07
 */

@Data
@XmlRootElement(name = "BBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class Body<T> {

    @XmlElement(name = "BodyHeader")
    private BodyHeader bodyHeader;

    @XmlAnyElement(lax = true)
    private T svcBody;


    @Data
    @XmlRootElement(name = "BodyHeader")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BodyHeader {

        @XmlElement(name = "TradeDate")
        private String tradeDate;

        @XmlElement(name = "TransNo")
        private String transNo;
    }

}
