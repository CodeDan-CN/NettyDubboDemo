package cn.wtu.zld.mapper;

import cn.wtu.zld.entity.User;

/**
 * 模拟数据库的操作
 * */
public class UserMapper {
    /**
     * 通过id查询指定的用户
     * @param id 用户id
     * @return User
     * */
    public User getUserIdToDatabase(int id){
        User user = new User(id, "CodeDan");
        return user;
    }

    /**
     * 通过id和新名称完成用户的信息修改,返回修改之后的对象
     * @param id
     * @param newUserName
     * @return User
     * */
    public User updateUserToDatabase(int id,String newUserName){
       return new User(id,newUserName);
    }
}
