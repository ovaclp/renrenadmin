package io.renren.common.config;

import io.renren.common.handler.SocketServerHandler;
import io.renren.common.utils.MsgCharsetCoderFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;


//@Service("minaClientConfig")
public class MinaClientConfig {

    //30秒后超时
    private static final int IDELTIMEOUT = 30;

	public IoSession session ;

	public static Map<String,Object> flagMap = new HashMap<String, Object>();

    //public Map<String, SocketConnector> socketConnectorMap = new ConcurrentHashMap<>();

    public static  Map<String , IoSession> sessionMap = new ConcurrentHashMap<>();

    /*public Map<String, IoSession> getSessionMap() {
        return sessionMap;
    }*/

    public IoSession getSession(String ip){
        return  MinaClientConfig.sessionMap.get(ip);
    }

    //	@PostConstruct
    public void connect(AddressipRecord addressipRecord) {
        //List<AddressipRecord> addressipRecords = getList();
        //for (AddressipRecord addressipRecord : addressipRecords) {
            SocketConnector connector = new NioSocketConnector();
            //socketConnectorMap.put(addressipRecord.address, connector);
           // socketConnectorMap.putIfAbsent(addressipRecord.address,connector);
            System.out.println("=================== 开始连接" + addressipRecord.port +"--"+addressipRecord.address+ " ================");
            connector.setDefaultRemoteAddress(new InetSocketAddress(addressipRecord.address, addressipRecord.port));
            conn(connector, addressipRecord.address);
        //}
    }


    private void conn(SocketConnector connector, String  ipaddress){
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        // 添加编码、编码过滤器
        filterChain.addLast("codec", new ProtocolCodecFilter(new MsgCharsetCoderFactory()));
        filterChain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        // 添加编码、编码过滤器
        // filterChain.addLast("codec", new ProtocolCodecFilter(new MsgCharsetCoderFactory()));
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
                        //sessionMap.put(ipaddress, session);

                        if(session.isConnected()){
                            InetSocketAddress inetSocketAddress= connector.getDefaultRemoteAddress();
                            System.out.println("断线重连["+ inetSocketAddress.getAddress() +":"+ inetSocketAddress.getPort()+"]成功,使用本地连接["+session.getLocalAddress()+"]");
                            MinaClientConfig.sessionMap.put(inetSocketAddress.getAddress().getHostName(),session);
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

        sessionConfig.setReadBufferSize(20480);//发送缓冲区
        sessionConfig.setReceiveBufferSize(20480);//接收缓冲区
        //建立连接
        ConnectFuture connectFuture= connector.connect();
        connectFuture.awaitUninterruptibly();
        try {
            session = connectFuture.getSession();
            MinaClientConfig.sessionMap.put(ipaddress, session);
        } catch (Exception e) {
            new Thread (new Runnable() {
                public void run() {
                    for(;;){
                        try{
                            Thread.sleep(3000);
                            ConnectFuture future = connector.connect();
                            future.awaitUninterruptibly();// 等待连接创建成功
                            session = future.getSession();// 获取会话
                            //MinaClientConfig.sessionMap.put(ipaddress, session);
                            if(session.isConnected()){
                                InetSocketAddress inetSocketAddress= connector.getDefaultRemoteAddress();
                                System.out.println("断线重连["+ inetSocketAddress.getAddress() +":"+ inetSocketAddress.getPort()+"]成功,使用本地连接["+session.getLocalAddress()+"]");
                                MinaClientConfig.sessionMap.put(inetSocketAddress.getAddress().getHostName(), session);
                                System.out.println("-------------------------------------------------------");

                                break;
                            }
                        }catch(Exception ex){
                            System.out.println("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
                        }
                    }
                }
            }).start();
        }
    }

}
