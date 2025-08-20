package cn.bravedawn.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-08-20 17:24
 */
@Data
public class CanalMessage<T> {

    /**
     * 数据库名
     */
    private String database;

    /**
     * 表名
     */
    private String table;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 变更数据
     */
    private List<T> data;

}