package cn.bravedawn.soapv2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @Author : depers
 * @Date : Created in 2026-01-13 19:20
 */
public class RemoveBBodyFilter extends XMLFilterImpl {


    @Override
    public void startElement(
            String uri, String localName, String qName, Attributes atts)
            throws SAXException {

        if ("BBody".equals(qName)) {
            // 进入 BBody，不向下游传 startElement
            return;
        }

        super.startElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(
            String uri, String localName, String qName)
            throws SAXException {

        if ("BBody".equals(qName)) {
            // 结束 BBody，不向下游传 endElement
            return;
        }

        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        // 字符内容正常透传
        super.characters(ch, start, length);
    }
}
