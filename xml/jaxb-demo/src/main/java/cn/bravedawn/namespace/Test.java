package cn.bravedawn.namespace;

import cn.bravedawn.XmlUtil;
import jakarta.xml.bind.JAXBException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 14:08
 */
public class Test {

    public static void main(String[] args) throws JAXBException {
        User user = new User(1L, "Tom");
        Goods goods = new Goods();
        goods.setId("1");
        goods.setName("apple");
        goods.setPrice("12");

        Goods goods1= new Goods();
        goods1.setId("2");
        goods1.setName("banana");
        goods1.setPrice("11");

        List<Goods> goodsList = new ArrayList<>();
        goodsList.add(goods1);
        goodsList.add(goods);
        user.setGoodsList(goodsList);

        String xml = XmlUtil.objToXMl(user, User.class, Goods.class);
        System.out.println(xml);
        String xml2 = xml.substring(102);
        System.out.println(xml2);

        //-======================================
        Result result = new Result<Goods>();
        result.setCode(1);
        result.setMsg("hello");
        result.setData(goods1);
        String xml3 = XmlUtil.objToXMl(result, Result.class, Goods.class);
        System.out.println(xml3);
    }
}
