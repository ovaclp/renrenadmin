package io.renren.common.utils;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgCharsetDecoder extends CumulativeProtocolDecoder {

	private static Logger log = LoggerFactory.getLogger(MsgCharsetDecoder.class);
	
	Charset charset=Charset.forName("gb2312");
	
    
    public MsgCharsetDecoder(Charset charset) {
        this.charset = charset;  
    }
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
		/*************开始解析数据******************/
		/**
		 *获取返回值长度
		 * 1	1	2	8	4	32	32	1	1	1	32	1   8   N	16
		 */

		if(buffer.limit()<130){
			return false;
		}
		byte[] temp=new byte[12];
		buffer.get(temp,0,12);

		byte[] length=new byte[4];
		buffer.get( length,0,4);
		//读取中间数据
		byte[] temp1=new byte[32];
		buffer.get(temp1,0,32);
		byte[] temp4=new byte[35];
		buffer.get(temp4,0,35);

		byte[] temp2=new byte[33];
		buffer.get(temp2,0,33);

		byte[] temp3=new byte[8];
		buffer.get(temp3,0,8);

		//获取数据内容
		String a=BytesUtil.bytesToHexString(length,0,4);
		int pudlen=Integer.parseInt(a.substring(0,2),16);
		System.out.println("---------"+pudlen+"长度-------");

		//毙杀长度
		if(pudlen==43){
			out.write(temp3);
			return true;

		}else{
			//转发基本信息,状态信息,日志信息
			if(pudlen>43){
				byte[] message=new byte[pudlen-43];
				buffer.get(message,0,pudlen-43);
				out.write(message);
				return true;
			}else{
				return false;
			}

		}

	}
}
