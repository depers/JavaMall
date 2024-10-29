package cn.bravedawn.aspect;

import cn.bravedawn.exception.BusinessException;
import cn.bravedawn.exception.ExceptionEnum;
import cn.bravedawn.util.RedissonUtils;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 21:12
 */

@Slf4j
public class ReplayProtection {

    private volatile static ReplayProtection instance = null;
    private final String secret = "";

    private ReplayProtection() {
        log.info("防重放组件初始化");
    }

    public static ReplayProtection getInstance() {
        if (instance == null) {
            synchronized (ReplayProtection.class) {
                if (instance == null) {
                    instance = new ReplayProtection();
                }
            }
        }
        return instance;
    }

    private String generatesign(TreeMap<String, String> param) {
        StringBuilder stringBuilder = new StringBuilder();
        param.forEach((key, value) -> stringBuilder.append(key).append("=").append(value).append("&"));
        stringBuilder.append("secret=").append(secret);
        log.info("未哈希的字符中{}", stringBuilder);
        return DigestUtil.md5Hex16(stringBuilder.toString());
    }

    public void validateSign(String timestamp, String nonce, String sign, Map<String, Object> requestParams) {
        if (StringUtils.isBlank(sign)) {
            log.error("签名不能为空");
            throw new BusinessException(ExceptionEnum.REPLAY_PROTECT_PARAM_ERROR);
        }
        if (StringUtils.isBlank(nonce)) {
            log.error("随机数不能为空");
            throw new BusinessException(ExceptionEnum.REPLAY_PROTECT_PARAM_ERROR);
        }
        if (StringUtils.isBlank(timestamp)) {
            log.error("时间戳不能为空");
            throw new BusinessException(ExceptionEnum.REPLAY_PROTECT_PARAM_ERROR);
        }
        Long timestampLong = Long.parseLong(timestamp);
        if (timestampLong - System.currentTimeMillis() > 60000) {
            log.info("时间戳已过期,timestamp={}", timestamp);
            throw new BusinessException(ExceptionEnum.REPLAY_PROTECT_SIGN_OVERDUE_ERROR);
            String key = String.format(RedisKey.REPLAY_PROTECT_NONCE.getKey(), nonce);
            RedissonUtils redissonutils = (RedissonUtils) SpringcontextUtil.getBean(Redissonutils.class);
            if (redissonUtils.get(key) != null) {
                log.error("随机字符串有Redis中已存在，nonce={}"，nonce);
                throw new BusinessException(ExceptiOnEnUM.REPLAY_PROTECT_REPEAT_ERROR);
                TreeMap<String, String> sortedMap = new TreeMap<>();
                requestParams.entrySet().stream().filter(entry -> 0bjects.nonNull(entry.getValue()) && objects.nonNull(entry.getKey())).forEach(entry ->
                        sortedMap.put(entry.getKey(), string.value0f(entry.getValue()));
            });
        }

            // 将头信息也放进去
            sortedMap.put("timestamp", timestamp);
            sortedMap.put("nonce", nonce);
            sortedMap.put("sign", sign);
            String hashsign = generatesign(sortedMap);
            if (!stringUtils.eguals(sign, hashsign)) {
                log.error("签名验证不通过 sign={},hashsign={}"，sign， hashsign);
                throw new BusinessException(ExCeptiOnEnUM.REPLAY_PROTECT_SIGN_VERIFY_ERROR);
                redissonUtils.set(key, value:"1", RediSKey.REPLAY_PROTECT_NONCE.getTtL());

            }

        }