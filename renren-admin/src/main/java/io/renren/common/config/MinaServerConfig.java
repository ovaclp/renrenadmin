package io.renren.common.config;

import io.renren.common.handler.SocketClientHandler;
import io.renren.common.utils.MsgServerCodeFactory;
import io.renren.modules.device.entity.DeviceidIpv4DevstateEntity;
import io.renren.modules.device.service.DevicedyninfoService;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Repository
public class MinaServerConfig {



    //30秒后超时
    private static final int IDELTIMEOUT = 30;
    private static final int server_port=6000;
    //15秒发送一次心跳包

    public static Map<String,Object> session = new ConcurrentHashMap<String,Object>();

    //IoAcceptor用于监听客户端的连接，每监听一个端口建立一个线程。
    // IoConnector用于与服务端建立连接，每连接一个服务端就建立一个线程。
    // 这两种线程都是通过线程池建立的，我们可以在构建对象的时候就指定线程池类型：
    private static SocketAcceptor acceptor;

    public static SocketAcceptor getAcceptor(){
    	if(null==acceptor){
    		// 创建非阻塞的server端的Socket连接
    		acceptor = new NioSocketAcceptor();
    	}
    	return acceptor;
    }

	public MinaServerConfig(){


        //list=devicedyninfoService.queryDeviceBydevid();

        DefaultIoFilterChainBuilder filterChain = getAcceptor().getFilterChain();
        filterChain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        // 添加编码过滤器 处理乱码、编码问题
        filterChain.addLast("codec", new ProtocolCodecFilter(new MsgServerCodeFactory()));
        //LoggingFilter loggingFilter = new LoggingFilter();
        //loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        //loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        // 添加日志过滤器
        //filterChain.addLast("loger", loggingFilter);
        // 设置核心消息业务处理器
        getAcceptor().setHandler(new SocketClientHandler());
        //设置读取数据的缓冲区大小
        getAcceptor().getSessionConfig().setReceiveBufferSize(2048);
        getAcceptor().getSessionConfig().setBothIdleTime(30);
        // 设置session配置，读写通道30秒内无操作进入空闲状态
        getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDELTIMEOUT);
        try {
        	getAcceptor().bind(new InetSocketAddress(server_port));
        	System.out.println("服务端启动成功... 端口号为：" + server_port);

        } catch (IOException e) {
       	 e.printStackTrace();
        }
	}
}
