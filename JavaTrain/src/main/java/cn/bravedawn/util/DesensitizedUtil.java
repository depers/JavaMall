package cn.bravedawn.util;

import cn.hutool.core.util.StrUtil;

/**
 * @Author : depers
 * @Date : Created in 2025-11-14 09:11
 */
public class DesensitizedUtil {

    /**
     * 脱敏字符串
     * @param str 字符串
     * @param prefixLen 保留前几位字符
     * @param suffixLen 保留后几位字符
     * @return
     */
    public static String desensitize(String str, int prefixLen, int suffixLen) {
        if (StrUtil.isBlank(str)) {
            return str;
        }

        return StrUtil.hide(str, prefixLen, str.length() - suffixLen);
    }

    /**
     * 脱敏中国姓名
     * @param str 字符串
     * @return
     */
    public static String chineseName(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }

        if (str.length() == 1) {
            return "*";
        } else if (str.length() == 2) {
            return desensitize(str, 1, 0);
        } else {
            return desensitize(str, 1, 1);
        }
    }
}
