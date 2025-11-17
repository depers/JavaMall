package cn.bravedawn.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-11-17 15:09
 */


public class MySQLTableSchemaReader {

    private Connection connection;

    public MySQLTableSchemaReader(String url, String username, String password)
            throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * 获取数据库中的所有表名
     */
    public List<String> getTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            tableNames.add(tableName);
        }
        tables.close();

        return tableNames;
    }

    /**
     * 获取指定表的字段结构信息
     */
    public List<ColumnInfo> getTableColumns(String tableName) throws SQLException {
        List<ColumnInfo> columns = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();

        // 获取字段基本信息
        ResultSet columnsRs = metaData.getColumns(null, null, tableName, "%");

        while (columnsRs.next()) {
            ColumnInfo column = new ColumnInfo();
            column.setTableName(tableName);
            column.setColumnName(columnsRs.getString("COLUMN_NAME"));
            column.setDataType(columnsRs.getString("TYPE_NAME"));
            column.setColumnSize(columnsRs.getInt("COLUMN_SIZE"));
            column.setNullable(columnsRs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
            column.setRemarks(columnsRs.getString("REMARKS"));
            column.setDefaultValue(columnsRs.getString("COLUMN_DEF"));
            column.setDecimalDigits(columnsRs.getInt("DECIMAL_DIGITS"));
            column.setOrdinalPosition(columnsRs.getInt("ORDINAL_POSITION"));

            columns.add(column);
        }
        columnsRs.close();

        // 获取主键信息
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeys.next()) {
            String pkColumnName = primaryKeys.getString("COLUMN_NAME");
            // 标记主键字段
            columns.stream()
                    .filter(col -> col.getColumnName().equals(pkColumnName))
                    .forEach(col -> col.setPrimaryKey(true));
        }
        primaryKeys.close();

        return columns;
    }

    /**
     * 获取完整的数据库结构（所有表）
     */
    public DatabaseSchema getDatabaseSchema() throws SQLException {
        DatabaseSchema schema = new DatabaseSchema();

        List<String> tableNames = getTableNames();
        for (String tableName : tableNames) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTableName(tableName);
            tableInfo.setColumns(getTableColumns(tableName));
            schema.addTable(tableInfo);
        }

        return schema;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * 字段信息实体类
     */
    public static class ColumnInfo {
        private String tableName;
        private String columnName;
        private String dataType;
        private int columnSize;
        private boolean nullable;
        private String remarks;
        private String defaultValue;
        private int decimalDigits;
        private int ordinalPosition;
        private boolean primaryKey;

        // getter 和 setter 方法
        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }

        public String getColumnName() { return columnName; }
        public void setColumnName(String columnName) { this.columnName = columnName; }

        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }

        public int getColumnSize() { return columnSize; }
        public void setColumnSize(int columnSize) { this.columnSize = columnSize; }

        public boolean isNullable() { return nullable; }
        public void setNullable(boolean nullable) { this.nullable = nullable; }

        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }

        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

        public int getDecimalDigits() { return decimalDigits; }
        public void setDecimalDigits(int decimalDigits) { this.decimalDigits = decimalDigits; }

        public int getOrdinalPosition() { return ordinalPosition; }
        public void setOrdinalPosition(int ordinalPosition) { this.ordinalPosition = ordinalPosition; }

        public boolean isPrimaryKey() { return primaryKey; }
        public void setPrimaryKey(boolean primaryKey) { this.primaryKey = primaryKey; }

        @Override
        public String toString() {
            return String.format("ColumnInfo{columnName='%s', dataType='%s', columnSize=%d, " +
                            "nullable=%s, remarks='%s', primaryKey=%s}",
                    columnName, dataType, columnSize, nullable, remarks, primaryKey);
        }
    }

    /**
     * 表信息实体类
     */
    public static class TableInfo {
        private String tableName;
        private List<ColumnInfo> columns = new ArrayList<>();

        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }

        public List<ColumnInfo> getColumns() { return columns; }
        public void setColumns(List<ColumnInfo> columns) { this.columns = columns; }

        public void addColumn(ColumnInfo column) { this.columns.add(column); }
    }

    /**
     * 数据库结构实体类
     */
    public static class DatabaseSchema {
        private List<TableInfo> tables = new ArrayList<>();

        public List<TableInfo> getTables() { return tables; }
        public void setTables(List<TableInfo> tables) { this.tables = tables; }

        public void addTable(TableInfo table) { this.tables.add(table); }
    }
}
