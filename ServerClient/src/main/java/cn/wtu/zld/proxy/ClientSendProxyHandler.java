package cn.wtu.zld.proxy;

import cn.wtu.zld.entity.User;
import cn.wtu.zld.netty.MyClientHandler;
import cn.wtu.zld.protoPOJO.MyMessage;
import cn.wtu.zld.services.UserServices;
import cn.wtu.zld.services.impi.UserServicesImpI;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用jdk自带的动态代理类，完成服务消费者需要调用远程服务的方法代理，通过代理编程完成原方法的修改
 * */
public class ClientSendProxyHandler implements InvocationHandler {

    //切面
    private MyClientHandler myClientHandler;

    public ClientSendProxyHandler(MyClientHandler myClientHandler) {
        this.myClientHandler = myClientHandler;
    }
    //通过线程池提供并发性能的同时利用channel的特性完成线程安全
    private ExecutorService executors = Executors.newFixedThreadPool(8);

    //完整的切面代码：用于获取切入点的各种信息封装成方法信息后传输给服务提供者
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取我们的方法参数(1.参数长度，2.参数，3.方法名)
        String methodName = method.getName();
        int length = args.length;
        MyMessage.Messsage.Builder builder = MyMessage.Messsage.newBuilder().setMethodName(methodName).setLength(length);
        for(int index = 0 ; index < length ; index++){
            if(args[index] instanceof Integer) {
                builder.addParaMeters(MyMessage.ParaMeter.newBuilder()
                        .setParaMeterType(MyMessage.ParaMeter.ParamType.intType).setIntData((int) args[index]).build());
            }else if(args[index] instanceof String){
                builder.addParaMeters(MyMessage.ParaMeter.newBuilder()
                        .setParaMeterType(MyMessage.ParaMeter.ParamType.stringType).setStringData((String) args[index]).build());
            }
        }
        MyMessage.Messsage messsage = builder.build();
        //设置参数
        myClientHandler.setMessage(messsage);
        //利用线程池进行发送，利用future和callable类进行结果数据的获取。
        return executors.submit(myClientHandler).get();
    }

//    public static void main(String[] args) {
//        UserServices userServices = (UserServices)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), UserServicesImpI.class.getInterfaces(), new ClientSendProxyHandler(null));
//        userServices.updateUserToServices(3,"codedan");
//        userServices.getIdToServices(4);
//    }
}
