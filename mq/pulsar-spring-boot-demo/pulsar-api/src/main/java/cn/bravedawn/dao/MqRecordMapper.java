package cn.bravedawn.dao;

import cn.bravedawn.core.MqRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息消费记录表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2025-07-22
 */
public interface MqRecordMapper extends BaseMapper<MqRecord> {

    int fetchTimeoutMessageCount(@Param("status") int status, @Param("sendMaxRetryCount") int sendMaxRetryCount,
                                 @Param("startTime") Date startTime);

    List<MqRecord> fetchTimeoutMessage(@Param("status") int status, @Param("lastTimeId") long lastTimeId,
                                       @Param("sendMaxRetryCount") int sendMaxRetryCount, @Param("offset") int offset,
                                       @Param("startTime") Date startTime);

    int updateRetryCount(@Param("id") Long id, @Param("createTime") Date createTime);
}
