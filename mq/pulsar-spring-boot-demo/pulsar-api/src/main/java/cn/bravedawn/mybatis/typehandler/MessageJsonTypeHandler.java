package cn.bravedawn.mybatis.typehandler;

import cn.bravedawn.core.PulsarMessage;
import cn.bravedawn.toolkit.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-28 17:33
 */
public class MessageJsonTypeHandler extends BaseTypeHandler<PulsarMessage> {


    /**
     * 如何将Java类型转换为数据库类型
     * @param preparedStatement
     * @param i
     * @param pulsarMessage
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, PulsarMessage pulsarMessage, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JsonUtil.toJson(pulsarMessage));
    }

    /**
     * 根据字段名从数据库获取时，如何将数据库类型转换为Java类型
     * @param resultSet
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public PulsarMessage getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = resultSet.getString(columnName);
        if (StringUtils.isNotBlank(value)) {
            return JsonUtil.toObj(value, PulsarMessage.class);
        }
        return null;
    }


    /**
     * 根据字段位置从数据库获取时，如何将数据库类型转换为Java类型
     * @param resultSet
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public PulsarMessage getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String value = resultSet.getString(columnIndex);
        if (StringUtils.isNotBlank(value)) {
            return JsonUtil.toObj(value, PulsarMessage.class);
        }
        return null;
    }


    /**
     * 在调用存储过程后将数据库类型转换为Java类型
     * @param callableStatement
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public PulsarMessage getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String value = callableStatement.getString(columnIndex);
        if (StringUtils.isNotBlank(value)) {
            return JsonUtil.toObj(value, PulsarMessage.class);
        }
        return null;
    }
}
