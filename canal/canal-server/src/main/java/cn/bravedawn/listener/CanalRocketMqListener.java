package cn.bravedawn.listener;

import cn.bravedawn.entity.CanalMessage;
import cn.bravedawn.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author : depers
 * @Date : Created in 2025-08-20 17:21
 */

@Slf4j
@Component
@RocketMQMessageListener(
        topic = "canal_topic",
        consumerGroup = "canal_group"
)
public class CanalRocketMqListener  implements RocketMQListener<CanalMessage<User>> {

    /**
     * canal发送到mq的消息内容：
     * {
     *   "data": [
     *     {
     *       "id": "5",
     *       "username": "马三明",
     *       "password": "%fqmhybp3",
     *       "email": "o.hnlu@hhyvqxbv.eg",
     *       "phone": "18192674843"
     *     }
     *   ],
     *   "database": "canal",
     *   "es": 1755682166000,
     *   "gtid": "",
     *   "id": 582,
     *   "isDdl": false,
     *   "mysqlType": {
     *     "id": "bigint unsigned",
     *     "username": "varchar(50)",
     *     "password": "varchar(50)",
     *     "email": "varchar(45)",
     *     "phone": "varchar(15)"
     *   },
     *   "old": [{ "username": "马二明" }],
     *   "pkNames": ["id"],
     *   "sql": "",
     *   "sqlType": {
     *     "id": -5,
     *     "username": 12,
     *     "password": 12,
     *     "email": 12,
     *     "phone": 12
     *   },
     *   "table": "user",
     *   "ts": 1755682166487,
     *   "type": "UPDATE"
     * }
     */

    @Override
    public void onMessage(CanalMessage<User> message) {
        String lineSeparator = System.lineSeparator();
        StringBuilder info = new StringBuilder(lineSeparator);
        info.append("==========数据变更信息==========").append(lineSeparator);
        info.append(String.format("数据库.表名: %s.%s%n", message.getDatabase(), message.getTable()));
        info.append(String.format("操作类型: %s%n", message.getType()));
        message.getData().forEach(user -> info.append(user).append(lineSeparator));
        log.info(info.toString());
    }
}
