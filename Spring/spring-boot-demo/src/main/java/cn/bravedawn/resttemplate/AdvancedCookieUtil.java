package cn.bravedawn.resttemplate;

import org.springframework.http.HttpHeaders;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : depers
 * @Date : Created in 2025-09-24 16:03
 */
public class AdvancedCookieUtil {

    public static List<AdvancedCookie> parseCookies(HttpHeaders headers) {
        List<AdvancedCookie> cookies = new ArrayList<>();
        List<String> cookieHeaders = headers.get(HttpHeaders.SET_COOKIE);

        for (String header : cookieHeaders) {
            AdvancedCookie cookie = parseCookie(header);
            if (cookie != null) {
                cookies.add(cookie);
            }
        }

        return cookies;
    }

    private static AdvancedCookie parseCookie(String cookieHeader) {
        String[] parts = cookieHeader.split(";");
        if (parts.length == 0) return null;

        AdvancedCookie cookie = new AdvancedCookie();

        // 解析名称和值
        String[] nameValue = parts[0].split("=", 2);
        if (nameValue.length == 2) {
            cookie.setName(nameValue[0].trim());
            cookie.setValue(nameValue[1].trim());
        }

        // 解析属性
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.equalsIgnoreCase("Secure")) {
                cookie.setSecure(true);
            } else if (part.equalsIgnoreCase("HttpOnly")) {
                cookie.setHttpOnly(true);
            } else if (part.toLowerCase().startsWith("domain=")) {
                cookie.setDomain(part.substring(7));
            } else if (part.toLowerCase().startsWith("path=")) {
                cookie.setPath(part.substring(5));
            } else if (part.toLowerCase().startsWith("expires=")) {
                // 简化处理，实际需要解析日期
                String httpDate = part.substring(8);
                DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(httpDate, formatter);
                // 转换为Date对象（如果需要）
                Date date = Date.from(zonedDateTime.toInstant());
                cookie.setExpires(new Date());
            } else if (part.toLowerCase().startsWith("max-age=")) {
                cookie.setMaxAge(Long.parseLong(part.substring(8)));
            }
        }

        return cookie;
    }
}
