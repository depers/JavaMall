package cn.bravedawn;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
/**
 * @Author : depers
 * @Date : Created in 2025-08-22 10:08
 */

/**
 * SUPPORT_DTD = false：完全禁用DTD支持，包括内部和外部实体
 *
 * IS_SUPPORTING_EXTERNAL_ENTITIES = false：只禁用外部实体，但允许内部实体
 */

public class XXEProtectionDemo {

    public static void main(String[] args) {
        // 包含XXE攻击的恶意XML
        String maliciousXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE root [\n" +
                "    <!ENTITY xxe SYSTEM \"file:///etc/passwd\">\n" +
                "]>\n" +
                "<root>&xxe;</root>";

        // 包含内部实体的XML（相对安全）
        String internalEntityXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE root [\n" +
                "    <!ENTITY internal \"Safe Internal Value\">\n" +
                "]>\n" +
                "<root>&internal;</root>";

        System.out.println("=== 测试恶意XML（包含外部实体）===");
        testConfiguration("默认配置", maliciousXml, false, false);
        testConfiguration("只禁用外部实体", maliciousXml, false, true);
        testConfiguration("完全禁用DTD", maliciousXml, true, false);

        System.out.println("\n=== 测试内部实体XML（相对安全）===");
        testConfiguration("默认配置", internalEntityXml, false, false);
        testConfiguration("只禁用外部实体", internalEntityXml, false, true);
        testConfiguration("完全禁用DTD", internalEntityXml, true, false);
    }

    private static void testConfiguration(String configName, String xml,
                                          boolean disableDtd, boolean disableExternalEntities) {
        System.out.println("\n--- " + configName + " ---");

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();

            if (disableDtd) {
                factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            }
            if (disableExternalEntities) {
                factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            }

            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(xml));

            StringBuilder content = new StringBuilder();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamReader.CHARACTERS) {
                    content.append(reader.getText());
                }
            }

            System.out.println("解析成功: " + content.toString());

        } catch (Exception e) {
            System.out.println("解析失败: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}