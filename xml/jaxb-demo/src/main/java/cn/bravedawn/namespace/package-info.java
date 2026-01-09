/**
 * @Author : depers
 * @Date : Created in 2026-01-09 11:29
 */


@jakarta.xml.bind.annotation.XmlSchema(
        namespace = "http://example.com/schema",
        elementFormDefault = jakarta.xml.bind.annotation.XmlNsForm.QUALIFIED,
        xmlns = {
                @jakarta.xml.bind.annotation.XmlNs(
                        prefix = "s",
                        namespaceURI = "http://example.com/schema"
                )
        }
)
package cn.bravedawn.namespace;