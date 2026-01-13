package cn.bravedawn.soapv2;

import cn.bravedawn.XmlUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;

/**
 * @Author : depers
 * @Date : Created in 2026-01-13 16:24
 */
public class Test {


    public static void main(String[] args) throws ParserConfigurationException, SAXException, JAXBException {

        Entity<User> entity = new Entity<>();
        EntityHeader entityHeader = new EntityHeader();
        entityHeader.setSysId("1202");

        User user = new User();
        user.setUsername("张三");
        user.setAge("18");

        Body.BodyHeader bodyHeader = new Body.BodyHeader();
        bodyHeader.setTradeDate("20260113");
        bodyHeader.setTransNo("20205101010");

        Body<User> body = new Body<>();
        body.setSvcBody(user);
        body.setBodyHeader(bodyHeader);

        entity.setEntityHeader(entityHeader);
        entity.setBody(body);
        String xml = XmlUtil.objToXMl(entity, Entity.class, User.class);
        System.out.println(xml);


        String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<Envelop>\n" + "    <Body>\n" + "        <BBody>\n" + "            <BodyHeader>\n" + "                <TradeDate>20260113</TradeDate>\n" + "                <TransNo>20205101010</TransNo>\n" + "            </BodyHeader>\n" + "            <svcBody>\n" + "                <Username>张三</Username>\n" + "                <Age>18</Age>\n" + "            </svcBody>\n" + "        </BBody>\n" + "    </Body>\n" + "    <EntityHeader>\n" + "        <SysId>1202</SysId>\n" + "    </EntityHeader>\n" + "</Envelop>";
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XMLReader reader = factory.newSAXParser().getXMLReader();

        // 设置过滤器
        RemoveBBodyFilter filter = new RemoveBBodyFilter();
        filter.setParent(reader);

        // JAXB
        JAXBContext context = JAXBContext.newInstance(Entity.class, User.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // 通过 SAXSource 解析
        SAXSource source = new SAXSource(filter, new InputSource(new StringReader(xml)));

        Entity<User> xmlEntity = (Entity<User>) unmarshaller.unmarshal(source);
        System.out.println(xmlEntity);

    }
}
