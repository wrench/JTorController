package com.mountainsofmars.jtorcontroller;

import org.jboss.netty.channel.*;
import org.apache.log4j.Logger;
import com.mountainsofmars.jtorcontroller.event.Event;
import java.util.Queue;
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
    TorListener listener;

    public TorProtocolHandler(Queue<Reply> replyQueue, TorListener listener) {
        this.replyQueue = replyQueue;
        this.listener = listener;
    }

	@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Reply reply = null;
        String msg = (String) e.getMessage();
        logger.info("Message received from TOR: " + msg);
        if(msg.startsWith("515")) {
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
        listener.sendEvent(Event.ON_CONNECT);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ex) {
        logger.error(ex.getCause().getMessage(), ex.getCause());
        replyQueue.offer(new FailureReply("FAIL"));
        channel = ex.getChannel();
        channel.close();
        listener.sendEvent(Event.ON_DISCONNECT);
        ex.getCause().printStackTrace();
    }

    public void sendMessage(String cmdString) {       
        channel.write(cmdString + " \r\n");
        logger.info("sent message: " + cmdString);
    }
}







