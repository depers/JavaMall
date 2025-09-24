package cn.bravedawn.resttemplate;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-09-24 15:20
 */
@Data
public class AdvancedCookie {

    private String name;
    private String value;
    private String domain;
    private String path;
    private Date expires;
    private boolean secure;
    private boolean httpOnly;
    private long maxAge;



    public static void main(String[] args) {
        String httpDate = "Wed, 24 Sep 2025 07:40:12 GMT";
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(httpDate, formatter);

        // 转换为Date对象（如果需要）
        Date date = Date.from(zonedDateTime.toInstant());
        System.out.println(date);
        System.out.println(DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN));
    }
}
