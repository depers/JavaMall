package cn.bravedawn.system.service.impl;

import cn.bravedawn.system.entity.MqRecord;
import cn.bravedawn.system.mapper.MqRecordMapper;
import cn.bravedawn.system.service.IMqRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息消费记录表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2025-07-22
 */
@Service
public class MqRecordServiceImpl extends ServiceImpl<MqRecordMapper, MqRecord> implements IMqRecordService {

}
