package cn.bravedawn.core;

import cn.bravedawn.mybatis.typehandler.MessageJsonTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息消费记录表
 * </p>
 *
 * @author baomidou
 * @since 2025-07-24
 */
@Getter
@Setter
@TableName("t_mq_record")
public class MqRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 消息内容
     */
    @TableField(value = "msg_content", typeHandler = MessageJsonTypeHandler.class)
    private PulsarMessage msgContent;

    /**
     * 重发次数
     */
    @TableField("try_count")
    private Integer tryCount;

    /**
     * 发送状态，0-发送中，1-发送成功，2-发送失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 下次重试时间
     */
    @TableField("next_retry_time")
    private Date nextRetryTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
}
