package com.mountainsofmars.jtorcontroller;

import org.apache.log4j.Logger;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import com.mountainsofmars.jtorcontroller.command.Command;
import com.mountainsofmars.jtorcontroller.reply.Reply;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
public class JTorController {

    private static final Logger logger = Logger.getLogger(Application.class);
    private TorProtocolHandler handler;
    private Queue<Reply> replyQueue;
    private String host;
    private int port;
    private TorCommunicator tCom;
    private Thread tCThread;
    private TorListener listener;

    public JTorController(String host, int port, TorListener listener) {
        replyQueue = new ArrayBlockingQueue<Reply>(1);
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    public void connect() {
        handler = new TorProtocolHandler(replyQueue, listener);
        tCom = new TorCommunicator(handler, host, port);
        tCThread = new Thread(tCom);
        tCThread.start();
    }

    public Reply authenticateNoPassword() {
        Reply reply = null;
        Command cmd = Command.AUTHENTICATE;
        return sendMsg(cmd.getCommandString());
    }
    
    public Reply authenticateHashedPassword(String password) {
    	Command cmd = Command.AUTHENTICATE;
    	String cmdString = cmd.getCommandString() + " \"" + password + "\"";
    	return sendMsg(cmdString);
    } 
    
    public Reply authenticateCookiePassword() {
    	//TODO Add method body.
    	Reply reply = null;
    	return reply;
    }
    
    public Reply setConf(String property, String value) {
    	//TODO add varargs for multiple property value pairs.
    	Command cmd = Command.SETCONF;
    	String cmdString = cmd.getCommandString() + " " + property + "=" + value;
    	return sendMsg(cmdString);
    }
    
    public Reply getConf(String property) {
    	Command cmd = Command.GETCONF;
    	String cmdString = cmd.getCommandString() + " " + property;
    	return sendMsg(cmdString);
    }
        
    private Reply sendMsg(String message) {
    	Reply reply = null;
    	handler.sendMessage(message);
    	try {
    		reply = ((ArrayBlockingQueue<Reply>) replyQueue).take(); 
    	} catch(InterruptedException ex) {
    		logger.error(ex.getCause().getMessage(), ex.getCause());
    	}
    	return reply;
    }
    
}