package cn.bravedawn.sharding;

import com.google.common.base.Strings;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.datanode.DataNodeInfo;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author : depers
 * @Date : Created in 2025-10-24 14:54
 */
@Slf4j
public class CustomSmsTableHashModShardingAlgorithm implements StandardShardingAlgorithm<String> {

    @Override
    public void init(Properties props) {

    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String sendTimeStr = preciseShardingValue.getValue().toString();
        String sendYear = sendTimeStr.substring(0, 4);
        DataNodeInfo dataNodeInfo = preciseShardingValue.getDataNodeInfo();
        String targetName = dataNodeInfo.getPrefix() + Strings.padStart(sendYear, dataNodeInfo.getSuffixMinLength(), dataNodeInfo.getPaddingChar());
        log.info("精确查询选择的分片是：targetName={}, availableTargetNames={}", targetName, collection);
        return collection.contains(targetName) ? targetName : null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {


        Range<String> valueRange = rangeShardingValue.getValueRange();
        int lower = 2019;
        int upper = 2025;
        if (valueRange.hasLowerBound()) {
            String lowerEndpoint = valueRange.lowerEndpoint();
            lower = Integer.parseInt(lowerEndpoint.substring(0, 4));
        }
        if (valueRange.hasUpperBound()) {
            String upperEndpoint = valueRange.upperEndpoint();
            upper = Integer.parseInt(upperEndpoint.substring(0, 4));
        }
        List<String> stringList = IntStream.rangeClosed(lower, upper)
                .boxed()
                .map(item -> "t_sms_" + item)
                .collect(Collectors.toList());

        log.info("范围查询选择的分片是：availableTargetNames={}", stringList);
        return stringList;
    }
}
