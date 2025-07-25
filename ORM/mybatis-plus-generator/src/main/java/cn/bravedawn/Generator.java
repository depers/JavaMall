package cn.bravedawn;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : mybatis-plus-generator
 * @Date : Created in 2025-07-22 10:08
 */
public class Generator {


    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/pulsar?characterEncoding=UTF-8&useSSL=false&serverTimeZone=Asia/Shanghai&rewriteBatchedStatements=true";
        String username = "root";
        String password = "fx1212";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            if (typeCode == Types.TINYINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            if (typeCode == Types.TIMESTAMP) {
                                // 自定义类型转换
                                return DbColumnType.DATE;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("cn.bravedawn") // 设置父包名
                                .moduleName("system") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, Paths.get(System.getProperty("user.dir")) + "/src/main/resources/mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude("t_mq_record") // 设置需要生成的表名
                                .addTablePrefix("t_", "c_") // 设置过滤表前缀
                                .entityBuilder()
                                .enableTableFieldAnnotation()
                                .enableLombok()
                                .enableFileOverride() // 覆盖实体类
                                // Mapper 策略
                                .mapperBuilder()
                                .enableFileOverride() // 覆盖 Mapper 接口
                                // Service 策略
                                .serviceBuilder()
                                .enableFileOverride() // 覆盖 Service 接口及实现
                                // Controller 策略
                                .controllerBuilder()
                                .enableFileOverride() // 覆盖 Controller 类
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
