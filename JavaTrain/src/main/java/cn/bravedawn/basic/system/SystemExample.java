package cn.bravedawn.basic.system;

/**
 * @Author : depers
 * @Date : Created in 2025-11-17 09:20
 */
public class SystemExample {

    /**
     * 获取不同操作系统的换行符
     *
     操作系统	换行符	Java表示	    说明
     Windows	CRLF	"\r\n"	    回车+换行
     Unix/Linux	LF	    "\n"	    换行
     经典Mac	    CR	    "\r"	    回车
     现代macOS	LF	    "\n"	    ｿ同Unix
     */

    public static void main(String[] args) {
        // 方式1: 使用系统属性
        String lineSeparator = System.getProperty("line.separator");
        System.out.println("换行符: " + escapeLineSeparator(lineSeparator));

        // 方式2: 直接获取系统换行符
        String systemLineSeparator = System.lineSeparator();
        System.out.println("系统换行符: " + escapeLineSeparator(systemLineSeparator));
    }

    // 将换行符转义为可读形式
    private static String escapeLineSeparator(String separator) {
        return separator.replace("\r", "\\r")
                .replace("\n", "\\n")
                + " (长度: " + separator.length() + ")";
    }
}
