package cn.bravedawn.timeout.dao;


import cn.bravedawn.timeout.model.AccountInfo;

public interface AccountInfoMapper {


    int deleteByPrimaryKey(Long id);

    int insert(AccountInfo record);

    int insertSelective(AccountInfo record);

    AccountInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountInfo record);

    int updateByPrimaryKey(AccountInfo record);
}