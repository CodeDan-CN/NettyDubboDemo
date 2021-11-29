package cn.wtu.zld.netty;

import cn.wtu.zld.protoPOJO.MyResult;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class ClientChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private MyClientHandler myClientHandler;

    public ClientChannelHandlerInitializer(MyClientHandler myClientHandler) {
        this.myClientHandler = myClientHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ProtobufDecoder(MyResult.Result.getDefaultInstance()));
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(myClientHandler);
    }
}
