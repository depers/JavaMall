package cn.bravedawn;

/**
 * @Author : depers
 * @Date : Created in 2025-08-22 10:48
 */
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "user")          // 指定为XML根元素
@XmlAccessorType(XmlAccessType.FIELD)   // 指定字段访问方式
public class User {

    @XmlElement(name = "username")      // 指定XML元素名
    private String name;

    @XmlAttribute(name = "userAge")     // 指定为XML属性而不是元素
    private int age;

    @XmlTransient                       // 忽略此字段，不参与XML转换
    private String password;
}