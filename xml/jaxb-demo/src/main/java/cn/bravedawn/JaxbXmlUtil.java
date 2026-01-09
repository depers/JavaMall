package cn.bravedawn;


import cn.bravedawn.namespace.XmlWrapper;
import jakarta.xml.bind.*;

import java.io.*;
import java.util.*;
/**
 * @Author : depers
 * @Date : Created in 2026-01-09 11:10
 */



public final class JaxbXmlUtil {

    private JaxbXmlUtil() {}

    /* ===================== 对象 → XML ===================== */

    public static String toXml(Object obj, Class<?>... boundClasses) {
        try {
            XmlWrapper<Object> wrapper = new XmlWrapper<>(obj);
            JAXBContext context = JAXBContext.newInstance(merge(XmlWrapper.class, boundClasses));

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter writer = new StringWriter();
            marshaller.marshal(wrapper, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("对象转 XML 失败", e);
        }
    }

    /* ===================== XML → 对象 ===================== */

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(String xml, Class<T> targetClass, Class<?>... boundClasses) {
        try {
            JAXBContext context = JAXBContext.newInstance(merge(XmlWrapper.class, boundClasses));
            Unmarshaller unmarshaller = context.createUnmarshaller();

            XmlWrapper<T> wrapper =
                    (XmlWrapper<T>) unmarshaller.unmarshal(new StringReader(xml));

            return wrapper.getValue();
        } catch (JAXBException e) {
            throw new RuntimeException("XML 解析失败", e);
        }
    }

    /* ===================== XML → List ===================== */

    @SuppressWarnings("unchecked")
    public static <T> List<T> fromXmlToList(
            String xml, Class<T> elementClass, Class<?>... boundClasses) {

        Object result = fromXml(xml, Object.class, boundClasses);

        if (result instanceof List<?>) {
            return (List<T>) result;
        }
        throw new IllegalArgumentException("XML 内容不是 List 类型");
    }

    /* ===================== 工具方法 ===================== */

    private static Class<?>[] merge(Class<?> root, Class<?>[] others) {
        Set<Class<?>> set = new LinkedHashSet<>();
        set.add(root);
        Collections.addAll(set, others);
        return set.toArray(new Class<?>[0]);
    }
}


