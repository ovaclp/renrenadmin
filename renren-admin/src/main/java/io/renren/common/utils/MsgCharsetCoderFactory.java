package io.renren.common.utils;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MsgCharsetCoderFactory implements ProtocolCodecFactory {

	private static final Charset charset=Charset.forName("gb2312");
	
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return new MsgCharsetDecoder(charset);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return new MsgCharsetEncoder(charset);
	}
	

}
