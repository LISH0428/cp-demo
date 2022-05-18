package com.example.cpdemo.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyDataSource extends MyAbstractDataSource {
    private final List<ConnectProxy> idleList = new ArrayList<ConnectProxy>();
    private final List<ConnectProxy> activeList = new ArrayList<ConnectProxy>();
    private int MaxActiveConn = 15;
    private int MaxIdleConn = 5;
    //最大等待时间
    private int MaxPoolTimeToWait = 30000;
    public final Object monitor = new Object();

    @Override
    public Connection getConnection() throws SQLException {
        //不使用代理類
        //return new ConnectProxy(this, super.getConnection()).getConnection();

        return getProxyConnection();
    }

    private Connection getProxyConnection() throws SQLException {
        ConnectProxy connectProxy = null;
        while (connectProxy == null) {
            synchronized (monitor) {
                //是否有空闲连接
                if (!idleList.isEmpty()) {
                    connectProxy= idleList.remove(0);
                    System.out.println(connectProxy.hashCode()+"从空闲队列中取出来了");
                } else {
                    //如果激活连接还有配额
                    if (activeList.size() < MaxActiveConn) {
                        connectProxy = new ConnectProxy(this, super.getConnection());
                        System.out.println(connectProxy.hashCode()+"被创建出来了");
                    }else{
                        try {
                            System.out.println("等待中");
                            monitor.wait(MaxPoolTimeToWait);
                            System.out.println("被唤醒了");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
        }
        if (connectProxy != null) {
            activeList.add(connectProxy);
            System.out.println(connectProxy.hashCode()+"加入了激活队列");
        }
        return connectProxy.getProxyConn();
    }

    public void closeConnection(ConnectProxy connectProxy)  {
        synchronized (monitor) {
            activeList.remove(connectProxy);
            if (idleList.size() < MaxIdleConn) {
                idleList.add(connectProxy);
                System.out.println(connectProxy.hashCode()+"使用完毕并放入了空闲队列");
                monitor.notify();
            }
        }
    }
}
