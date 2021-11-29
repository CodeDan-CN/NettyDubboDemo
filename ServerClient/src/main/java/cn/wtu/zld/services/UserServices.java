package cn.wtu.zld.services;

import cn.wtu.zld.entity.User;

public interface UserServices {
    /**
     * 通过id查询指定的用户
     * @param id 用户id
     * @return User
     * */
    public User getIdToServices(int id);

    /**
     * 通过id和新名称完成用户的信息修改,返回修改之后的对象
     * @param id
     * @param newUserName
     * @return User
     * */
    public User updateUserToServices(int id, String newUserName);

}
