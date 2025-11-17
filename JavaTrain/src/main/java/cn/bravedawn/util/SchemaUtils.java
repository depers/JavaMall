package cn.bravedawn.util;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-11-17 15:08
 */
public class SchemaUtils {

    /**
     * 生成建表语句格式的输出
     */
    public static void printCreateTableFormat(MySQLTableSchemaReader reader, String tableName)
            throws SQLException {

        List<MySQLTableSchemaReader.ColumnInfo> columns = reader.getTableColumns(tableName);

        System.out.println("CREATE TABLE `" + tableName + "` (");

        for (int i = 0; i < columns.size(); i++) {
            MySQLTableSchemaReader.ColumnInfo col = columns.get(i);
            StringBuilder sb = new StringBuilder();

            sb.append("  `").append(col.getColumnName()).append("` ");
            sb.append(col.getDataType());

            if (col.getColumnSize() > 0) {
                sb.append("(").append(col.getColumnSize());
                if (col.getDecimalDigits() > 0) {
                    sb.append(",").append(col.getDecimalDigits());
                }
                sb.append(")");
            }

            if (!col.isNullable()) {
                sb.append(" NOT NULL");
            }

            if (col.getDefaultValue() != null) {
                sb.append(" DEFAULT '").append(col.getDefaultValue()).append("'");
            }

            if (col.isPrimaryKey()) {
                sb.append(" PRIMARY KEY");
            }

            if (i < columns.size() - 1) {
                sb.append(",");
            }

            if (col.getRemarks() != null && !col.getRemarks().isEmpty()) {
                sb.append(" COMMENT '").append(col.getRemarks()).append("'");
            }

            System.out.println(sb);
        }

        System.out.println(");");
    }

    /**
     * 导出为Markdown表格
     */
    public static void exportAsMarkdown(MySQLTableSchemaReader reader, String tableName)
            throws SQLException {

        List<MySQLTableSchemaReader.ColumnInfo> columns = reader.getTableColumns(tableName);

        System.out.println("## 表: " + tableName);
        System.out.println();
        System.out.println("| 字段名 | 类型 | 长度 | 可空 | 主键 | 默认值 | 备注 |");
        System.out.println("|--------|------|------|------|------|--------|------|");

        for (MySQLTableSchemaReader.ColumnInfo col : columns) {
            System.out.printf("| %s | %s | %d | %s | %s | %s | %s |%n",
                    col.getColumnName(),
                    col.getDataType(),
                    col.getColumnSize(),
                    col.isNullable() ? "是" : "否",
                    col.isPrimaryKey() ? "是" : "否",
                    col.getDefaultValue() != null ? col.getDefaultValue() : "",
                    col.getRemarks() != null ? col.getRemarks() : "");
        }
    }


    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://192.168.24.128:3306/pulsar?useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "fx1212";
        MySQLTableSchemaReader mySQLTableSchemaReader = new MySQLTableSchemaReader(url, username, password);
        exportAsMarkdown(mySQLTableSchemaReader, "t_mq_record");
        System.out.println();
        printCreateTableFormat(mySQLTableSchemaReader, "t_mq_record");
    }
}