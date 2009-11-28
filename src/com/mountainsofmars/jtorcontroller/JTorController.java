package com.mountainsofmars.jtorcontroller;

import org.apache.log4j.Logger;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import com.mountainsofmars.jtorcontroller.command.Command;
import com.mountainsofmars.jtorcontroller.reply.FailureReply;
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
    
    public Reply authenticateHashedPassword(String password) { // Might should change this to an overloaded version.
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
    	Command cmd = Command.SETCONF; //TODO Add varargs for multiple property value pairs.
    	String cmdString = cmd.getCommandString() + " " + property + "=" + value;
    	return sendMsg(cmdString);
    }
    
    public Reply getConf(String property) {
    	Command cmd = Command.GETCONF;
    	String cmdString = cmd.getCommandString() + " " + property;
    	return sendMsg(cmdString);
    }
    
    public Reply setEvents(String... properties) { //TODO Floods BQ even with one keyword.
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
    
    public Reply signal(String property) { //TODO Add varargs for multiple properties.
    	Command cmd = Command.SIGNAL;
    	String cmdString = cmd.getCommandString() + " " + property;
    	return sendMsg(cmdString);
    }
    
    public Reply mapAddress(String oldAddress, String newAddress) {
    	//TODO Implement method.
    	return new FailureReply("Fix ME");
    }
    
    public Reply getInfo(String keyword) { //TODO Floods BQ even with one keyword. Add varargs for multiple keywords.
    	Command cmd = Command.GETINFO;
    	String cmdString = cmd.getCommandString() + " " + keyword;
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