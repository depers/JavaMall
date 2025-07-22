package cn.bravedawn.druid.model;

/**
 * <p>Title: Role </p>
 * <p>Description: 角色pojo</p>
 * @author : fengx9
 * @version : v1.0
 * @date : 2020/10/23 16:14
 */

public class Role {

    private long id;
    private String roleName;
    private String note;
    private Byte type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", note='" + note + '\'' +
                ", type=" + type +
                '}';
    }
}
