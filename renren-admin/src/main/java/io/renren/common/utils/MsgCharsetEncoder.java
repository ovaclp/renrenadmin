package io.renren.common.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.coyote.http2.ByteUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgCharsetEncoder extends ProtocolEncoderAdapter  {

	private static Logger log = LoggerFactory.getLogger(MsgCharsetEncoder.class);

    public static AtomicInteger i = new AtomicInteger(1);

	Charset charset=Charset.forName("gb2312");
	  
    public MsgCharsetEncoder() {  
        this(Charset.defaultCharset());  
    }  
    
    public MsgCharsetEncoder(Charset charset) {  
        this.charset = charset;  
    }
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		
		MessageData messageData = (MessageData)message;
		
		int totalLen = new String(messageData.getMessage()).length()+98;
		//预分配内存空间
		IoBuffer buffer = IoBuffer.allocate(totalLen).setAutoExpand(true);
		/**
		 * 消息ID
		 */
		byte[] messageid = CodeUtils.hexStringToBytes(messageData.getMessageid());
		int orderLen = messageid.length;
		if(orderLen!=8){
			log.error("（PC--SM）8byte，消息ID长度不正确："+orderLen);
			return;
		}
		//解决重放攻击导致接口无法使用
		byte[] a=BytesUtil.intToByteArray(i.incrementAndGet());
		messageid[7]=a[0];

		/**
		 * 目的方ID
		 */
		byte[] destinyid = CodeUtils.hexStringToBytes(messageData.getDestinyid());
		int destinylength = destinyid.length;
		if(destinylength!=32){
			log.error("（PC--SM）32byte，目的方ID长度不正确："+orderLen);
			return;
		}
		/**
		 * 发送方ID
		 */
		byte[] sendid = CodeUtils.hexStringToBytes(messageData.getSendid());
		int sendlen = sendid.length;
		if(sendlen!=32){
			log.error("（PC--SM）32byte，发送方ID长度不正确："+orderLen);
			return;
		}
		/**
		 * 消息尾
		 */
		byte[] mac = CodeUtils.hexStringToBytes(messageData.getMac());
		int maclen = mac.length;
		if(maclen!=16){
			log.error("（PC--SM）16byte，消息尾长度不正确："+orderLen);
			return;
		}

		buffer.put( CodeUtils.hexStringToBytes(messageData.getVersion()));//版本
		buffer.put( CodeUtils.hexStringToBytes(messageData.getSecurity()));//安全模式

		buffer.put(CodeUtils.hexStringToBytes(messageData.getHold()));//保留
		buffer.put(messageid);//消息ID
		buffer.put(CodeUtils.hexStringToBytes(messageData.getLength()));//pdu长度
		//		目的方ID（监控中心）
		buffer.put(destinyid);
		//		发送方ID（代理）
		buffer.put(sendid);
		//		操作类型（0xA3）
		buffer.put(CodeUtils.hexStringToBytes(messageData.getOprtype()));
		//		密码设备管理应用标识（0x00）
		buffer.put(CodeUtils.hexStringToBytes(messageData.getAppid()));
		buffer.put(CodeUtils.hexStringToBytes(messageData.getType()));
		buffer.put(CodeUtils.hexStringToBytes(messageData.getDevcieid()));
		//		密码设备监控消息PDU
		buffer.put(messageData.getMessage());
		//				安全通道会话密钥对消息头和消息PDU计算的16字节的MAC值
		buffer.put(mac);
		/**
		 * 写数据
		 */
		buffer.flip();
//		buffer1.flip();
		out.write(buffer);
		out.flush();
		buffer.free();
//		buffer1.free();
	}
}
