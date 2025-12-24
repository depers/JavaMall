package cn.bravedawn;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-12-24 15:03
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BookList {

    @XmlElement(name = "Book")
    private List<Book> bookList;
}
