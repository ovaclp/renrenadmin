package io.renren.common.config;

import io.renren.common.handler.SocketServerHandler;
import io.renren.common.utils.MessageData;
import io.renren.common.utils.MsgCharsetCoderFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TCPConnector {
    public  IoConnector connector = null;
    public  IoSession session = null;

    public IoSession getSession() {
        return session;
    }

    public TCPConnector(String ip, int port) throws Exception{
        connector = new NioSocketConnector();
        connector.getSessionConfig().setUseReadOperation(true);
        connector.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new MsgCharsetCoderFactory()));
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        connector.setHandler(new SocketServerHandler());
        ConnectFuture connectF = connector.connect(new InetSocketAddress(ip,
                port));
        connectF.awaitUninterruptibly();
        if(connectF.isDone()){
            if (!connectF.isConnected()){
                this.connector.dispose();
                throw new Exception();
            }else{
                session = connectF.getSession();
            }
        }
    }


    public  Object sendMsg(MessageData message) {
        long endTime1=System.currentTimeMillis();
        WriteFuture writeF = session.write(message);
        writeF.awaitUninterruptibly();
        long endTime2=System.currentTimeMillis();
        System.out.println("时间"+(endTime2-endTime1)+"ms,");
        if (writeF.getException() != null) {
            return writeF.getException().getMessage();
        } else if (writeF.isWritten()) {
            // 发送、接受
            ReadFuture readF = session.read().awaitUninterruptibly();
            if (readF.getException() != null) {
                //System.out.println(readF.getException().getMessage());
                return readF.getException().getMessage();
            } else {
                //System.out.println("[client]"+readF.getMessage());
                return readF.getMessage();
            }
        } else {
            //System.out.println("error!");
            return "error";
        }
    }

   /* public  Object sendMsg(MessageData message) {
        WriteFuture writeF = session.write(message);
        writeF.awaitUninterruptibly();
        ReadFuture readF=null;
        if(writeF.isWritten()){
            readF = session.read().awaitUninterruptibly();

        }
        return readF.getMessage();
    }*/


    public void close() {
        this.connector.dispose();
    }
}
