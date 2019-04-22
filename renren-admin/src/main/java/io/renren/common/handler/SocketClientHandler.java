package io.renren.common.handler;


import org.apache.mina.core.service.IoHandlerAdapter;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketClientHandler extends IoHandlerAdapter {


    protected static Logger logger = LoggerFactory.getLogger(SocketClientHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {


    }

}
