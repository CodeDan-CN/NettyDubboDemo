package cn.wtu.zld.services.impi;

import cn.wtu.zld.entity.User;
import cn.wtu.zld.mapper.UserMapper;
import cn.wtu.zld.services.UserServices;

public class UserServicesImpI implements UserServices {

    private UserMapper userMapper = new UserMapper();

    @Override
    public User getIdToServices(int id) {
        return userMapper.getUserIdToDatabase(id);
    }

    @Override
    public User updateUserToServices(int id, String newUserName) {
        return userMapper.updateUserToDatabase(id,newUserName);
    }
}
