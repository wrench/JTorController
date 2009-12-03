package com.mountainsofmars.jtorcontroller;

import org.jboss.netty.channel.*;
import org.apache.log4j.Logger;
import java.util.Queue;

import com.mountainsofmars.jtorcontroller.listenerevent.TorListenerEvent;
import com.mountainsofmars.jtorcontroller.listenerevent.TorListenerEventType;
import com.mountainsofmars.jtorcontroller.reply.*;

@ChannelPipelineCoverage("one")
public class TorProtocolHandler extends SimpleChannelHandler {

    private static final Logger logger = Logger.getLogger(TorProtocolHandler.class);
    private Channel channel;
    private Queue<Reply> replyQueue;
    private boolean getInfoMode;
    private StringBuilder tempSB; // This holds the multiline responses to a GETINFO call until the final "250 OK" is received.
    
    public TorProtocolHandler(Queue<Reply> replyQueue) {
        this.replyQueue = replyQueue;
        getInfoMode = false;
    }

	@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Reply reply = null;
        String msg = (String) e.getMessage();
        StringBuilder sb = null;
        logger.info("Message received from TOR: " + msg);
        if(msg.startsWith("650")) {
        	EventDispatcher.fireEvent(new TorListenerEvent(TorListenerEventType.INFO_MESSAGE, msg));
        	return;
        } else if(msg.startsWith("250")) {
        	if(!getInfoMode) {
        		reply = new SuccessReply(msg); 
        	} else { // This must be a multiline response to a GETINFO call.
        		if(msg.startsWith("250 OK")) { // The multiline response to a GETINFO call is complete.
        			reply = new SuccessReply(tempSB.toString());
        			tempSB = null; // Reset tempSB variable to null.
        			this.getInfoMode = false;
        		}
        		if(tempSB == null) { // First line of a multiline GETINFO call.
        			tempSB = new StringBuilder(msg);
        			tempSB.append(" ");
        		} else { // Not the first line of a multiline GETINFO call.
        			tempSB.append(msg);
        			tempSB.append(" ");
        		}
        	}
        } else {
        	reply = new FailureReply(msg);
        }
        if(!getInfoMode) {
        	replyQueue.add(reply);
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        channel = e.getChannel();
        logger.info("Connected!");
        EventDispatcher.fireEvent(new TorListenerEvent(TorListenerEventType.ON_CONNECT));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ex) {
        logger.error(ex.getCause().getMessage(), ex.getCause());
        replyQueue.offer(new FailureReply("Exception Caught!"));
        channel = ex.getChannel();
        channel.close();
        EventDispatcher.fireEvent(new TorListenerEvent(TorListenerEventType.ON_DISCONNECT));
        ex.getCause().printStackTrace();
    }

    public void sendMessage(String cmdString) {       
        channel.write(cmdString + " \r\n");
        logger.info("sent message: " + cmdString);
    }
    
    public void setGetInfoMode() {
    	this.getInfoMode = true;
    }
}







