package io.renren.common.config;

import java.net.InetSocketAddress;

import io.renren.common.handler.SocketServerHandler;
import io.renren.common.utils.MsgCharsetCoderFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.*;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClientConfignew {

    //30秒后超时
    /*private static final int IDELTIMEOUT = 30;

    public IoSession session;
    public String address;
    public int port;

    public IoSession init (String address,int port){

        NioSocketConnector connector=new NioSocketConnector();
        connector.setDefaultRemoteAddress(new InetSocketAddress(address, port));
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        filterChain.addLast("threadPool", new ExecutorFilter(new OrderedThreadPoolExecutor()));
        // 添加编码、编码过滤器
        filterChain.addLast("codec", new ProtocolCodecFilter(new MsgCharsetCoderFactory()));
        // 添加日志过滤器
        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        filterChain.addLast("loger", loggingFilter);
        //添加断线重连过滤器
        filterChain.addFirst("reconnection", new IoFilterAdapter(){
            @Override
            public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {
                for(;;){
                    try{
                        connector.getManagedSessions();
                        System.out.println(connector.getManagedSessionCount());
                        Thread.sleep(3000);
                        ConnectFuture future = connector.connect();
                        future.awaitUninterruptibly();// 等待连接创建成功
                        session = future.getSession();// 获取会话
                        if(session.isConnected()){
                            InetSocketAddress inetSocketAddress= connector.getDefaultRemoteAddress();
                            System.out.println("断线重连["+ inetSocketAddress.getAddress() +":"+ inetSocketAddress.getPort()+"]成功,使用本地连接["+session.getLocalAddress()+"]");
                            break;
                        }
                    }catch(Exception ex){
                        System.out.println("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
                    }
                }
            }
        });
        // 设置核心消息业务处理器
        connector.setHandler(new SocketServerHandler());
        connector.setConnectTimeoutMillis(IDELTIMEOUT*1000);
        //session设置
        SocketSessionConfig sessionConfig =connector.getSessionConfig();
        //sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, IDELTIMEOUT);
        sessionConfig.setReuseAddress(true);

        //建立连接
        ConnectFuture connectFuture= connector.connect();
        connectFuture.awaitUninterruptibly();
        try {
            session = connectFuture.getSession();
        } catch (Exception e) {
            new Thread (new Runnable() {
                public void run() {
                    for(;;){
                        try{
                            Thread.sleep(3000);
                            ConnectFuture future = connector.connect();
                            future.awaitUninterruptibly();// 等待连接创建成功
                            session = future.getSession();// 获取会话
                            if(session.isConnected()){
                                InetSocketAddress inetSocketAddress= connector.getDefaultRemoteAddress();
                                System.out.println("断线重连["+ inetSocketAddress.getAddress() +":"+ inetSocketAddress.getPort()+"]成功,使用本地连接["+session.getLocalAddress()+"]");
                                break;
                            }
                        }catch(Exception ex){
                            System.out.println("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
                        }
                    }
                }
            }).start();
        }
        return session;
    }*/
}
