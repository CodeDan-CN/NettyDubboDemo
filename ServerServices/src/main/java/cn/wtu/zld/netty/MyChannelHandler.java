package cn.wtu.zld.netty;

import cn.wtu.zld.entity.User;
import cn.wtu.zld.protoPOJO.MyMessage;
import cn.wtu.zld.protoPOJO.MyResult;
import cn.wtu.zld.services.UserServices;
import cn.wtu.zld.services.impi.UserServicesImpI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义Handler，主要负责解析服务消费者发来的方法信息，然后进行对应服务的提供之后，返回服务结果值。
 * */
public class MyChannelHandler extends SimpleChannelInboundHandler<MyMessage.Messsage> {
    //真正的服务调用对象，为了模拟真实环境，采用单例的形式
    private static UserServicesImpI userServicesImpI = new UserServicesImpI();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessage.Messsage messsage) throws Exception {
//        System.out.println("客户端发来信息"+messsage);
        //我们开始解析从服务消费者那边传输过来的信息
        Object result = getParameterFromMessage(messsage);
        //回发给服务消费者，封装一下结果,我们采用HTTP协议的状态码，404和200
        rcvMessage(channelHandlerContext,result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //出现异常的时候，关闭通道
        ctx.close();
    }

    /**
     *  把解析从服务消费者那边传输过来的信息业务逻辑提取出来
     * @param messsage proto格式message对象
     */
    public Object getParameterFromMessage(MyMessage.Messsage messsage) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        //首先获取方法名
        String methodName = messsage.getMethodName();
        //然后获取参数个数
        int parameterNumber = messsage.getLength();
        //最后获取指定的参数,parametes用来存储参数
        Object[] parametes = new Object[parameterNumber];
        for(int index = 0 ; index < parameterNumber ; index++){
            MyMessage.ParaMeter paraMeter = messsage.getParaMeters(index);
            if(paraMeter.getParaMeterType() == MyMessage.ParaMeter.ParamType.intType){
                parametes[index] = paraMeter.getIntData();
            }else if(paraMeter.getParaMeterType() == MyMessage.ParaMeter.ParamType.stringType){
                parametes[index] = paraMeter.getStringData();
            }
        }
//        System.out.println("此方法为"+methodName+"，参数个数为"+parameterNumber+"，参数为"+ Arrays.toString(parametes));
        //我们通过反射来确定具体的方法
        Class<? extends UserServices> servicesImpIClass = userServicesImpI.getClass();
//        System.out.println("结果"+execFunction(servicesImpIClass, methodName, parametes));
        return execFunction(servicesImpIClass, methodName, parametes);
    }

    /**
     * 通过提取的数据来通过反射执行对应的方法，没找到改方法返回null
     * @param className 对应业务逻辑类的类对象
     * @param methodName 方法名称
     * @param parameters 参数列表
     * */
    public Object execFunction(Class<?> className, String methodName, Object[] parameters) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        //通过迭代后判断名称决定执行对应方法
        Method[] methods = className.getMethods();
        for(Method method : methods){
            if(method.getName().equals(methodName)){
                return method.invoke(userServicesImpI,parameters);
            }
        }
        return null;
    }

    /**
     * 把执行完毕的结果封装为protobuf对象后进行回复
     * @param ctx 对应客户端的channel节点
     * @param msg 执行结果
     * */
    public void rcvMessage(ChannelHandlerContext ctx , Object msg){
        User tempResult = (User)msg;
        int state = 404;
        MyResult.Result result;
        if(msg != null){
            state = 200;
            result = MyResult.Result.newBuilder().setState(state)
                    .setUser(MyResult.User.newBuilder()
                            .setUserId(tempResult.getUserId())
                            .setUserName(tempResult.getUserName())).build();
        }else{
            result = MyResult.Result.newBuilder().setState(state).build();
        }
        ctx.writeAndFlush(result);
    }

//    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
////        new MyChannelHandler().rcvMessage(null,new User(1,"codedan"));
////        构建一个Message测试一下
//        MyMessage.ParaMeter paraMeter1 = MyMessage.ParaMeter.newBuilder()
//                .setParaMeterType(MyMessage.ParaMeter.ParamType.intType)
//                .setIntData(11809).build();
//        MyMessage.ParaMeter paraMeter2 = MyMessage.ParaMeter.newBuilder()
//                .setParaMeterType(MyMessage.ParaMeter.ParamType.stringType)
//                .setStringData("CodeDan").build();
//        MyMessage.Messsage messsage = MyMessage.Messsage.newBuilder().setLength(2).setMethodName("updateUserToServices")
//                .addParaMeters(0, paraMeter1)
//                .addParaMeters(1, paraMeter2).build();
//        System.out.println(messsage);
//        new MyChannelHandler().getParameterFromMessage(messsage);
//    }
}
