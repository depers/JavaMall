package cn.bravedawn.obj.proxy.jdk;

/**
 * @Author : depers
 * @Date : Created in 2025-12-30 20:47
 */
public class UserServiceImpl implements UserService {

    @Override
    public void save(String name) {
        System.out.println("保存用户：" + name);
    }
}

