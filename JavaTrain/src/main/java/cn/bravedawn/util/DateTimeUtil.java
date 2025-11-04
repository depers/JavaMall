package cn.bravedawn.util;

import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @Author : depers
 * @Date : Created in 2025-10-28 10:55
 */
public class DateTimeUtil {


    /**
     * 校验日期格式
     */
    public static boolean validate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        TemporalAccessor temporalAccessor = formatter.parseUnresolved(dateStr, new ParsePosition(0));
        return temporalAccessor != null;
    }



}
