package io.renren.common.handler;


import io.renren.common.config.AddressipRecord;
import io.renren.common.config.MinaClientConfig;
import io.renren.common.config.MinaServerConfig;
import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.device.entity.AgentinfoEntity;
import io.renren.modules.device.service.AgentinfoService;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SocketServerHandler extends IoHandlerAdapter{

	private static Logger log = LoggerFactory.getLogger(SocketServerHandler.class);

	@Autowired
	private MinaClientConfig clientConfig;
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		//InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
		//clientConfig.getSessionMap().put(inetSocketAddress.getPort(),session);
		System.out.println("创建一个新连接："+ session.getRemoteAddress()+"  id:  "+session.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("发生异常..."+cause.getMessage()+"||"+session.getId());
		CloseFuture closeFuture = session.close(true);
		closeFuture.addListener(new IoFutureListener<IoFuture>() {
			public void operationComplete(IoFuture future) {
				if (future instanceof CloseFuture) {
					((CloseFuture) future).setClosed();
					System.out.println("sessionClosed CloseFuture setClosed-->"+ future.getSession().getId());
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		/*InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
		String  ipinfo = inetSocketAddress.getHostName();
		if (MinaClientConfig.flagMap.get(ipinfo)==null){
			MinaClientConfig  minaClientConfig =(MinaClientConfig)SpringContextUtils.getBean("minaClientConfig");
			AddressipRecord addressipRecord=new AddressipRecord();
			addressipRecord.setAddress(ipinfo);
			AgentinfoService agentinfoService = (AgentinfoService)SpringContextUtils.getBean("agentinfoService");
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("AgentIP",ipinfo);
			List<AgentinfoEntity> agentinfoEntities =  agentinfoService.selectByMap(param);
			if (agentinfoEntities!=null && agentinfoEntities.size()>0){
				addressipRecord.setPort(agentinfoEntities.get(0).getAgentport());
				minaClientConfig.connect(addressipRecord);
				MinaClientConfig.flagMap.put(ipinfo,"1");
			}
		}*/



	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
		MinaServerConfig.session.remove(inetSocketAddress.getHostName());
		log.info("设备["+inetSocketAddress.getHostName()+"]断开连接，端口号为："+inetSocketAddress.getPort());
		System.out.println("关闭当前session: "+session.getId()+session.getRemoteAddress());
		CloseFuture closeFuture = session.close(true);
		closeFuture.addListener(new IoFutureListener<IoFuture>() {
			public void operationComplete(IoFuture future) {
				if (future instanceof CloseFuture) {
					((CloseFuture) future).setClosed();
					System.out.println("sessionClosed CloseFuture setClosed-->"+ future.getSession().getId());
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
		MinaServerConfig.session.put(inetSocketAddress.getHostName(), session);
		log.info("设备["+inetSocketAddress.getHostName()+"]链接空闲，端口号为："+inetSocketAddress.getPort());
		System.out.println("当前连接处于空闲状态："+ session.getRemoteAddress()+ status);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
		MinaServerConfig.session.put(inetSocketAddress.getHostName(), session);
		log.info("设备["+inetSocketAddress.getHostName()+"]建立连接，端口号为："+inetSocketAddress.getPort());
		System.out.println("打开一个session id："+ session.getId()+"  空闲连接个数IdleCount：  "+ session.getBothIdleCount());
	}
}
