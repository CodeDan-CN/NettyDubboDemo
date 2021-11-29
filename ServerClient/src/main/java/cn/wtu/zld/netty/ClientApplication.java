package cn.wtu.zld.netty;

/**
 * 模拟spring boot写法，编写服务消费者启动类
 * */
public class ClientApplication {
    public static void main(String[] args) {
        new ClientBootStrap("127.0.0.1",8080).start();
    }
}
