package cn.bravedawn.insert.controller;

import cn.bravedawn.dao.UserMapper;
import cn.bravedawn.service.UserService;
import cn.bravedawn.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : depers
 * @program : mybatis-batch-insert
 * @date : Created in 2024/8/9 20:22
 */

@RestController
@Slf4j
public class InsertController {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 一个一个插入数据
     * 1000条数据不配置rewriteBatchedStatements参数：500ms
     * 1000条数据配置rewriteBatchedStatements参数：520ms
     * @return
     */
    @GetMapping("oneByOneInsert")
    public String oneByOneInsert() {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");

        long startTime = System.currentTimeMillis();
        log.info("开始插入数据");
        for(int i = 0; i < 1000; i++) {
            // 这里是每次插入开一个事务
            userMapper.insertSelective(user);
        }

        log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
        return "success";
    }


    /**
     * 一个一个插入数据库，在一个事务中
     * 1000条数据不配置rewriteBatchedStatements参数：420ms
     * 1000条数据配置rewriteBatchedStatements参数：400ms
     * @return
     */
    @GetMapping("oneByOneInsertV2")
    public String oneByOneInsertV2() {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");

        long startTime = System.currentTimeMillis();

        log.info("开始插入数据");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for(int i = 0; i < 1000; i++) {
                    // 这里是每次插入开一个事务
                    userMapper.insertSelective(user);
                }
            }
        });

        log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
        return "success";
    }


    /**
     * 使用Mybatis
     * 1000笔不配置rewriteBatchedStatements参数：95ms
     * 1000笔配置rewriteBatchedStatements参数：100ms
     *
     * 100000笔不配置rewriteBatchedStatements参数：5355ms
     * 100000笔配置rewriteBatchedStatements参数：5189ms
     * @return
     */
    @GetMapping("batchInsert")
    public String batchInsert() {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");

        List<User> userList = new ArrayList<>();

        for(int i = 0; i < 100000; i++) {
            userList.add(user);
        }

        long startTime = System.currentTimeMillis();
        log.info("开始插入数据");
        userMapper.batchInsert(userList);
        log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
        return "success";
    }


    /**
     *
     * 使用JDBC
     * 1000笔不配置rewriteBatchedStatements参数：100ms
     * 1000笔配置rewriteBatchedStatements参数：50ms
     *
     * 100000笔不配置rewriteBatchedStatements参数：6000ms
     * 100000笔配置rewriteBatchedStatements参数：2420ms
     * @return
     * @throws SQLException
     */
    @GetMapping("batchInsertV2")
    public String batchInsertV2() throws SQLException {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");

        List<User> userList = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        log.info("开始插入数据");
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setAutoCommit(false);
        String sql = "insert into user (name, age, gender, phone, family_address, email, marital_status, date_of_birth, education_background, create_user, update_user) values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            for(int i = 0; i < 1000; i++) {

                statement.setString(1, user.getName());
                statement.setInt(2, user.getAge());
                statement.setInt(3, user.getGender());
                statement.setString(4, user.getPhone());
                statement.setString(5, user.getFamilyAddress());
                statement.setString(6, user.getEmail());
                statement.setInt(7, user.getMaritalStatus());
                statement.setString(8, user.getDateOfBirth());
                statement.setInt(9, user.getEducationBackground());
                statement.setString(10, user.getCreateUser());
                statement.setString(11, user.getUpdateUser());
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
        } finally {
            statement.close();
            connection.close();
        }
        log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
        return "success";
    }


    /**
     *
     * 使用Mybatis-plus
     * 1000笔不配置rewriteBatchedStatements参数：203ms
     * 1000笔配置rewriteBatchedStatements参数：152ms
     *
     * 100000笔不配置rewriteBatchedStatements参数：14240ms
     * 1000笔配置rewriteBatchedStatements参数：11277ms
     * @return
     */
    @GetMapping("batchInsertV3")
    public String batchInsertV3() {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");
        user.setInsertTime(new Date());
        user.setUpdateTime(new Date());

        List<User> userList = new ArrayList<>();

        for(int i = 0; i < 100000; i++) {
            userList.add(user);
        }

        long startTime = System.currentTimeMillis();
        log.info("开始插入数据");
        userService.saveBatch(userList);
        log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
        return "success";
    }

    /**
     * MyBatis针对批量插入的优化
     * @return
     * @throws SQLException
     */
    @GetMapping("MybatisBatchInsert")
    public String MybatisBatchInsert() throws SQLException {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");
        user.setInsertTime(new Date());
        user.setUpdateTime(new Date());

        List<User> userList = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            userList.add(user);
        }

        long startTime = System.currentTimeMillis();
        log.info("开始插入数据");
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            for (User item : userList) {
                mapper.insertSelective(item);
            }

            sqlSession.commit();
            log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
            return "success";
        }catch(Throwable e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        return "success";
    }


    /**
     * 使用SqlSession的Batch模式添加了Spring事务
     * @return
     * @throws SQLException
     */
    @GetMapping("MybatisBatchInsertV2")
    public String MybatisBatchInsertV2() throws SQLException {
        User user = new User();
        user.setName("冯晓");
        user.setAge(27);
        user.setGender(1);
        user.setPhone("17393164120");
        user.setFamilyAddress("陕西省西安市长安区融发心园");
        user.setEmail("dev_fengxiao@163.com");
        user.setMaritalStatus(0);
        user.setDateOfBirth("1996-11-30");
        user.setEducationBackground(1);
        user.setCreateUser("system");
        user.setUpdateUser("system");
        user.setInsertTime(new Date());
        user.setUpdateTime(new Date());

        List<User> userList = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            userList.add(user);
        }

        long startTime = System.currentTimeMillis();
        log.info("开始插入数据");
        SqlSession sqlSession = null;
        TransactionManager transactionManager = new TransactionManager();
        try {
            insert(userList);
            log.info("开始睡眠");
            Thread.sleep(20000);
            userMapper.insertSelective(user);
            transactionManager.commit();
            log.info("插入数据耗时：{}ms", System.currentTimeMillis() - startTime);
            return "success";
        }catch(Throwable e) {
            log.error("出现异常", e);
            transactionManager.rollback();
        }
        return "success";
    }

    private void insert(List<User> userList) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            for (User item : userList) {
                mapper.insertSelective(item);
            }

            // 这里会将事务托管给spring统一处理
            sqlSession.commit();
        }
    }

}
