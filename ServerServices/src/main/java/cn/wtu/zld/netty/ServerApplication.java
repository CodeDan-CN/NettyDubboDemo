package cn.wtu.zld.netty;


/**
 * 用户信息服务启动类
 * */
public class ServerApplication {
    public static void main(String[] args) {
        new ServerBootStrap(8080).listen();
    }
}
