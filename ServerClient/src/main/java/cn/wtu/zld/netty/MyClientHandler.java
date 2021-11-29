package cn.wtu.zld.netty;

import cn.wtu.zld.entity.Message;
import cn.wtu.zld.entity.ResultBody;
import cn.wtu.zld.entity.User;
import cn.wtu.zld.protoPOJO.MyMessage;
import cn.wtu.zld.protoPOJO.MyResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * 服务消费者自定义Handler，用于发送方法信息和接受返回结果
 * */
public class MyClientHandler extends SimpleChannelInboundHandler<MyResult.Result> implements Callable<User> {

    //方法信息参数
    private MyMessage.Messsage message;
    //返回结果参数
    private ResultBody resultBody;
    //当前客户端对应channel节点对象
    private ChannelHandlerContext channelHandlerContext;

    public void setMessage(MyMessage.Messsage message) {
        this.message = message;
    }

    public void setResultBody(ResultBody resultBody) {
        this.resultBody = resultBody;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext){
        this.channelHandlerContext =channelHandlerContext;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, MyResult.Result result) throws Exception {
        //接受信息，进行信息解析，然后封装为MyResult对象
        int state = result.getState();
        if(state == 200){
            int userId = result.getUser().getUserId();
            String userName = result.getUser().getUserName();
            resultBody = new ResultBody(state,new User(userId,userName));
        }else{
            resultBody = new ResultBody(state,null);
        }
        //通过线程之间的通信完成信息的顺序获取，使用发送后wait和接收后notify可以完成信息的顺序发送和接收。
        //不然直接执行完毕时，结果还会空。
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelHandlerContext = ctx;
    }

    //发送信息方法
    @Override
    public synchronized User call() throws Exception {
        //通过线程之间的通信完成信息的顺序获取，使用发送后wait和接收后notify可以完成信息的顺序发送和接收。
        //不然直接执行完毕时，结果还会空。
        channelHandlerContext.writeAndFlush(message);
        wait();
        if(resultBody.getState() == 200){
            return resultBody.getUser();
        }
        return null;
    }
}
