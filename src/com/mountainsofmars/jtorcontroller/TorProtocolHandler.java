package com.mountainsofmars.jtorcontroller;

import org.jboss.netty.channel.*;
import org.apache.log4j.Logger;
import java.util.concurrent.BlockingQueue;
import com.mountainsofmars.jtorcontroller.reply.*;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
@ChannelPipelineCoverage("one")
public class TorProtocolHandler extends SimpleChannelHandler {

    private static final Logger logger = Logger.getLogger(TorProtocolHandler.class);
    private Channel channel;
    private BlockingQueue<Reply> replyQueue;
    TorListener listener;

    public TorProtocolHandler(BlockingQueue<Reply> replyQueue, TorListener listener) {
        this.replyQueue = replyQueue;
        this.listener = listener;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String msg = (String) e.getMessage();
        logger.info("Message received from TOR: " + msg);
        replyQueue.offer(new SuccessReply(msg));
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        channel = e.getChannel();
        logger.info("Connected!");
        listener.onConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ex) {
        logger.error(ex.getCause().getMessage(), ex.getCause());
        replyQueue.offer(new FailureReply("FAIL"));
        channel = ex.getChannel();
        channel.close();
        listener.onDisconnect();
        ex.getCause().printStackTrace();
    }

    public void sendMessage(String cmdString) {       
        //channel.write(cmdString + "\r\n");
        channel.write("AUTHENTICATE\r\n");
        logger.info("sent message: " + cmdString);
    }

    private FailureReply resolveErrorReply(String response) {
        FailureReply reply = null;
        if(response.equals("Connection refused")) {
            
        }
        return reply;
    }
}







