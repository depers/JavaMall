package cn.bravedawn.druid.dao;


import cn.bravedawn.druid.model.Role;

/**
 * <p>Title: RoleMapper </p>
 * <p>Description: 角色mapper</p>
 * @author : fengx9
 * @version : v1.0
 * @date : 2020/10/23 16:17
 */

public interface RoleMapper {

    public Role getRole(Long id);
    public Role findRole(String roleName);
    public int deleteRole(Long id);
    public int insertRole(Role role);
    public long insertAndSelectKey(Role role);
}
