package com.mountainsofmars.jtorcontroller;

import org.jboss.netty.channel.*;
import org.apache.log4j.Logger;
import java.util.Queue;

import com.mountainsofmars.jtorcontroller.listenerevent.ListenerEvent;
import com.mountainsofmars.jtorcontroller.reply.*;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
@ChannelPipelineCoverage("one")
public class TorProtocolHandler extends SimpleChannelHandler {

    private static final Logger logger = Logger.getLogger(TorProtocolHandler.class);
    private Channel channel;
    private Queue<Reply> replyQueue;
    
    public TorProtocolHandler(Queue<Reply> replyQueue) {
        this.replyQueue = replyQueue;
    }

	@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Reply reply = null;
        String msg = (String) e.getMessage();
        logger.info("Message received from TOR: " + msg);
        if(!msg.startsWith("250")) {
        	reply = new FailureReply(msg);
        	
        } else {
        	reply = new SuccessReply(msg);      		
        }
        replyQueue.add(reply);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        channel = e.getChannel();
        logger.info("Connected!");
        EventDispatcher.fireEvent(ListenerEvent.ON_CONNECT);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ex) {
        logger.error(ex.getCause().getMessage(), ex.getCause());
        replyQueue.offer(new FailureReply("Exception Caught!"));
        channel = ex.getChannel();
        channel.close();
        EventDispatcher.fireEvent(ListenerEvent.ON_DISCONNECT);
        ex.getCause().printStackTrace();
    }

    public void sendMessage(String cmdString) {       
        channel.write(cmdString + " \r\n");
        logger.info("sent message: " + cmdString);
    }
}







