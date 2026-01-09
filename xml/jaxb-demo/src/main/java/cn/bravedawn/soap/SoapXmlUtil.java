package cn.bravedawn.soap;

import java.util.Map;

/**
 * @Author : depers
 * @Date : Created in 2026-01-09 14:21
 */
public class SoapXmlUtil {


    private static final String  xml_label = "<s:%1$s>%2$s</s:%1$s>";

    public static String generateXml() throws IllegalAccessException {
        User user = new User();
        user.setUsername("fengxiao");

        Map<String, String> fieldXmlNameMap = JaxbFieldUtil.getFieldXmlNameMap(user);
        fieldXmlNameMap.forEach((key, val) -> {
            System.out.println(String.format(xml_label, key, val));
        });

        return "";
    }


    public static void main(String[] args) throws IllegalAccessException {
        generateXml();
    }
}
