package cn.bravedawn.druid.service.impl;

import cn.bravedawn.druid.dao.RoleMapper;
import cn.bravedawn.druid.model.Role;
import cn.bravedawn.druid.service.DruidService;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author : depers
 * @program : druid
 * @description: DruidService实现类
 * @date : Created in 2021/7/31 19:20
 */
@Service
public class DruidServiceImpl implements DruidService {

    @Autowired
    private RoleMapper roleMapper;



    @Override
    public long selectKey() {
        Role role = new Role();
        role.setRoleName("test");
        role.setNote("selectKey");
        roleMapper.insertAndSelectKey(role);
        return role.getId();
    }

    @Override
    public int add() {
        Log log = LogFactory.getLog(DruidServiceImpl.class);
        log.error("test-------------------------------");
        int result = 0;
        Role role = new Role();
        role.setRoleName("test");
        role.setNote("selectKey");
        try{
            result = roleMapper.insertRole(role);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                System.out.println("DuplicateKeyException-----------------------");
            } else {
                System.out.println(e.getClass() + "-----------------------");
            }
        }
        return result;
    }
}
