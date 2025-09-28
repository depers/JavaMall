package cn.bravedawn.controlleradvice.advice;

import cn.bravedawn.controlleradvice.config.RequestFilterConfig;
import cn.bravedawn.controlleradvice.util.ApplicationContextHolder;
import cn.hutool.core.text.AntPathMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 14:17
 */

@Slf4j
public class EncryptControlUtil {

    /**
     * 判断是否需要加解密
     * @return
     */
    public static boolean checkEncrypt() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String requestURI = request.getRequestURI();

        RequestFilterConfig requestFilterConfig = ApplicationContextHolder.getBean(RequestFilterConfig.class);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        Optional<String> anyMather = requestFilterConfig.getFilterUrlList().stream().filter(item -> pathMatcher.match(item, requestURI)).findAny();
        if (requestFilterConfig.isEnable() && anyMather.isPresent() && request.getMethod().equalsIgnoreCase("post")) {
            log.info("请求方式[{}], 请求地址[{}], 匹配的配置规则[{}]", request.getMethod(), requestURI, anyMather.get());
            return true;
        } else {
            log.info("请求方式[{}], 请求地址[{}], 不需要进行加解密处理", request.getMethod(), requestURI);
            return false;
        }
    }


}
