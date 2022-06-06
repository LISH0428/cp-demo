package com.example.cpdemo.pool;

import lombok.Data;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.sql.Connection;
@Data
public class ConnectProxy implements MethodInterceptor {

    private MyDataSource myDataSource;
    //被代理的源对象
    private Connection connection;
    //代理后的对象
    private Connection proxyConn;

    public ConnectProxy() {
    }
    public ConnectProxy(MyDataSource myDataSource, Connection connection) {
        this.myDataSource = myDataSource;
        this.connection = connection;
        this.proxyConn = getProxy(connection.getClass());
    }

    //创建通用代理 的方法
    public <T> T  getProxy(Class clazz){
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(clazz);

        enhancer.setCallback(this);
        return (T)enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        //System.out.println("method======"+methodName);
        if ("close".equalsIgnoreCase(methodName)){
                //把连接归还连接池
                myDataSource.closeConnection(this);
                return null;

        }
        else{
            return methodProxy.invoke(connection, objects);
            //return methodProxy.invokeSuper(o, objects); 会空指针异常？
        }
        //更通俗的理解:
        //invokeSuper调用的是被代理类的方法, 但只有代理类才存在基类, 必须使用代理类作为obj参数调用
        //invoke调用的是增强方法, 必须使用被代理类的对象调用, 使用代理类会造成OOM

    }
}
