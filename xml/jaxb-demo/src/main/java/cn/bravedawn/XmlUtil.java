package cn.bravedawn;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @Author : depers
 * @Date : Created in 2025-08-22 10:27
 */
@Slf4j
public class XmlUtil {


    /**
     * 对象转字符串
     * @param obj
     * @param classes
     * @return
     */
    public static String objToXMl(Object obj, Class<?>... classes) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // 将对象转为xml
            StringWriter sw = new StringWriter();
            marshaller.marshal(obj, sw);
            return sw.toString();
        }catch (JAXBException e) {
            log.error("序列化xml异常", e);
        }
        return null;
    }

    /**
     * xml字符串转对象
     * @param xml
     * @param classes
     * @return
     */
    public static Object xmlToObj(String xml, Class<?>... classes) {
        try {
            StringReader reader = new StringReader(xml);
            JAXBContext context = JAXBContext.newInstance(classes);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            log.error("xml转指定类型对象失败", e);
        }
        return null;
    }


    /**
     * 获取xml中特定标签的内容
     * @param xmlStr
     * @param tagName
     * @return
     */
    public static String getTagContent(String xmlStr, String tagName) {
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(xmlStr));
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT && tagName.equals(reader.getLocalName())) {
                    return reader.getElementText();
                }
            }
        }  catch (XMLStreamException e) {
            log.info("获取xml中指定标签的内容异常", e);
        }
        return null;
    }
}
