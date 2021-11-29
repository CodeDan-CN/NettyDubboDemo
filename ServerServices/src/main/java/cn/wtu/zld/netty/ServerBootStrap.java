package cn.wtu.zld.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 定义服务提供者通信结构
 * */
public class ServerBootStrap {
    //监听端口
    private int port;

    public ServerBootStrap(int port) {
        this.port = port;
    }

    public void listen(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        ServerBootstrap bootstrap = new ServerBootstrap();
        try{
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置操作系统层面，最多TCP连接数128个
                    .option(ChannelOption.SO_BACKLOG,128)
                    //开启长连接机制
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ServerChannelHandlerInitializer());
            //异步处理本次监听
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
