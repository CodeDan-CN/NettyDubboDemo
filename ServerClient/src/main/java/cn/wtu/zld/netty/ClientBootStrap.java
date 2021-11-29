package cn.wtu.zld.netty;

import cn.wtu.zld.entity.User;
import cn.wtu.zld.proxy.ClientSendProxyHandler;
import cn.wtu.zld.services.UserServices;
import cn.wtu.zld.services.impi.UserServicesImpI;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Proxy;

public class ClientBootStrap {

    private String ip ;
    private int port;

    public ClientBootStrap(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    /**
     * 此方法负责开启服务消费者，并模拟服务消费者进行服务的消费
     * */
    public void start(){
        MyClientHandler clientHandler = new MyClientHandler();
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelHandlerInitializer(clientHandler));
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            //开始编写代理操作代码
            UserServices userServices = (UserServices)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), UserServicesImpI.class.getInterfaces(),new ClientSendProxyHandler(clientHandler));
            User result1 = userServices.getIdToServices(1);
            User result2 = userServices.updateUserToServices(11809, "CodeDan");
            System.out.println("getIdToServices方法结果是"+result1);
            System.out.println("updateUserToServices方法结果是"+result2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
