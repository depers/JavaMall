package cn.bravedawn.resttemplate;


import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @Author : depers
 * @Date : Created in 2025-09-24 15:05
 */


public class CookieUtils {

    /**
     * 从响应头中提取Cookie
     */
    public static Map<String, String> extractCookiesFromHeaders(HttpHeaders headers) {
        Map<String, String> cookies = new HashMap<>();
        List<String> cookieHeaders = headers.get(HttpHeaders.SET_COOKIE);

        if (cookieHeaders != null) {
            for (String cookieHeader : cookieHeaders) {
                parseCookieHeader(cookieHeader, cookies);
            }
        }

        return cookies;
    }

    /**
     * 解析Cookie头信息
     */
    private static void parseCookieHeader(String cookieHeader, Map<String, String> cookies) {
        String[] cookiePairs = cookieHeader.split(";");
        for (String pair : cookiePairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                // 只处理基本的键值对，忽略过期时间等属性
                if (!key.equalsIgnoreCase("Path") &&
                        !key.equalsIgnoreCase("Domain") &&
                        !key.equalsIgnoreCase("Expires") &&
                        !key.equalsIgnoreCase("Max-Age") &&
                        !key.equalsIgnoreCase("Secure") &&
                        !key.equalsIgnoreCase("HttpOnly")) {
                    cookies.put(key, value);
                }
            }
        }
    }

    /**
     * 将Cookie Map转换为请求头格式
     */
    public static String formatCookiesForHeader(Map<String, String> cookies) {
        return cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("; "));
    }
}
