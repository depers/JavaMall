package cn.bravedawn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 短信表
 * </p>
 *
 * @author baomidou
 * @since 2025-10-24
 */
@Data
@TableName("t_sms")
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 批次id
     */
    @TableField("batchid")
    private String batchid;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 短信内容
     */
    @TableField("message")
    private String message;

    /**
     * 短信状态，0-未发送，1-发送成功，2-发送失败
     */
    @TableField("state")
    private Integer state;

    @TableField("send_time")
    private String sendTime;

    /**
     * 优先级
     */
    @TableField("priority")
    private Long priority;
}
