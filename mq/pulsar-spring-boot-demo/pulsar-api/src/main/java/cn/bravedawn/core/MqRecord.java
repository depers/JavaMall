package cn.bravedawn.core;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息消费记录表
 * </p>
 *
 * @author baomidou
 * @since 2025-07-22
 */
@TableName("t_mq_record")
@Data
public class MqRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 重发次数
     */
    private Integer tryCount;

    /**
     * 发送状态，0-发送中，1-发送成功，2-发送失败
     */
    private Integer status;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MqRecord{" +
            "id = " + id +
            ", msgContent = " + msgContent +
            ", tryCount = " + tryCount +
            ", status = " + status +
            ", nextRetryTime = " + nextRetryTime +
            ", createUser = " + createUser +
            ", createTime = " + createTime +
            ", updateUser = " + updateUser +
            ", updateTime = " + updateTime +
        "}";
    }
}
