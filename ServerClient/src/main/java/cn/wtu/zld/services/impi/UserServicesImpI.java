package cn.wtu.zld.services.impi;


import cn.wtu.zld.entity.User;
import cn.wtu.zld.services.UserServices;

public class UserServicesImpI implements UserServices {
    //服务消费者这边并不编写业务逻辑代码，真正实现在服务提供者服务中。
    @Override
    public User getIdToServices(int id) {
        return null;
    }

    @Override
    public User updateUserToServices(int id, String newUserName) {
        return null;
    }
}
