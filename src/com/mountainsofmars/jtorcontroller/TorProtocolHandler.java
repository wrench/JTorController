package com.mountainsofmars.jtorcontroller;

import org.jboss.netty.channel.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
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
    private List<String> tempMessages; // This holds the multiline responses to a GETINFO call until the final "250 OK" is received.
    
    public TorProtocolHandler(Queue<Reply> replyQueue) {
        this.replyQueue = replyQueue;
        getInfoMode = false;
    }

	@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Reply reply = null;
        String msg = (String) e.getMessage();
        logger.info("Message received from TOR: " + msg);
        if(msg.startsWith("650")) {
        	EventDispatcher.fireEvent(new TorListenerEvent(TorListenerEventType.INFO_MESSAGE, msg));
        	return;
        } else if(msg.startsWith("250") || !msg.startsWith("551") && !msg.startsWith("552")) { // This must be a 250 prefixed message or a GETINFO message that does NOT begin with a numerical code.
        	if(!getInfoMode) {
        		reply = new SuccessReply(msg); 
        	} else { // This must be a multiline response to a GETINFO call.
        		if(msg.startsWith("250 OK")) { // The multiline response to a GETINFO call is complete.
        			tempMessages.add(msg);
        			reply = new SuccessReply(tempMessages);
        			tempMessages = null; // Reset tempSB variable to null.
        			this.getInfoMode = false;
        		}
        		if(tempMessages == null) { // First line of a multiline GETINFO call.
        			tempMessages = new ArrayList<String>();
        			tempMessages.add(msg);
        		} else { // Not the first line of a multiline GETINFO call.
        			tempMessages.add(msg);
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







