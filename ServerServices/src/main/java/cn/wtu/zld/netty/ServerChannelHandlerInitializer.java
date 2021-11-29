package cn.wtu.zld.netty;

import cn.wtu.zld.protoPOJO.MyMessage;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * 初始化Handle过程，此过程给channel对应的Pipeline中添加顺序入站和顺序出站的编解码Handler和自定义Handler
 * */
public class ServerChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //获取通道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加ProtoBuf解码器
        pipeline.addLast(new ProtobufDecoder(MyMessage.Messsage.getDefaultInstance()));
        pipeline.addLast(new ProtobufEncoder());
        //添加自定义Handler处理
        pipeline.addLast(new MyChannelHandler());
    }
}
