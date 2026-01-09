package cn.bravedawn.soap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.Data;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 14:20
 */
@Data
public class User {


    @XmlElement(name = "UserName")
    private String username;

    @XmlElementWrapper(name = "goods")   // 外层 <items>
    @XmlElement(name = "item")            // 内层 <item>
    private List<Goods> goods;
}
