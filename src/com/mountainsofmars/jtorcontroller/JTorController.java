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
    private EventDispatcher eventDispatcher;
    private TorProtocolHandler handler;
    private Queue<Reply> replyQueue;
    private String host;
    private int port;
    private TorCommunicator tCom;
    private Thread tCThread;

    public JTorController(String host, int port, TorListener listener) {
    	eventDispatcher = new EventDispatcher();
    	EventDispatcher.addListener(listener);
    	eventDispatcher.start();
        replyQueue = new ArrayBlockingQueue<Reply>(1);
        this.host = host;
        this.port = port;
    }

    public void connect() {
        handler = new TorProtocolHandler(replyQueue);
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
    
    public Reply setEvents(String... properties) {
    	Command cmd = Command.SETEVENTS;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(String property : properties) {
    		cmdString.append(" ");
    		cmdString.append(property);
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply saveConf() {
    	Command cmd = Command.SAVECONF;
    	String cmdString = cmd.getCommandString();
    	return sendMsg(cmdString);
    }
    
    public Reply signal(String property) {
    	Command cmd = Command.SIGNAL;
    	String cmdString = cmd.getCommandString() + " " + property + " NEWNYM";
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