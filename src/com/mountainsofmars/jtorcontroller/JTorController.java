package com.mountainsofmars.jtorcontroller;

import org.apache.log4j.Logger;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import com.mountainsofmars.jtorcontroller.reply.FailureReply;
import com.mountainsofmars.jtorcontroller.reply.Reply;
import com.mountainsofmars.jtorcontroller.setevent.SetEvent;
import com.mountainsofmars.jtorcontroller.signal.Signal;
import com.mountainsofmars.jtorcontroller.torcommand.TorCommand;

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

    public Reply authenticate() { // 3.5
        TorCommand cmd = TorCommand.AUTHENTICATE;
        return sendMsg(cmd.getCommandString());
    }
    
    public Reply authenticate(String password) { // 3.5
    	TorCommand cmd = TorCommand.AUTHENTICATE;
    	String cmdString = cmd.getCommandString() + " \"" + password + "\"";
    	return sendMsg(cmdString);
    } 
    
    public Reply authenticateCookiePassword() { // 3.5
    	//TODO Add method body. This does not work.
    	Reply reply = null;
    	return reply;
    }
    
    public Reply setConf(String keyword, String value) { // 3.1
    	TorCommand cmd = TorCommand.SETCONF; //TODO Add varargs for multiple property value pairs, maybe?
    	String cmdString = cmd.getCommandString() + " " + keyword + "=" + value;
    	return sendMsg(cmdString);
    }
    
    public Reply resetConf(String keyword) { // 3.2
    	TorCommand cmd = TorCommand.RESETCONF;
    	String cmdString = cmd.getCommandString() + " " + keyword;
    	return sendMsg(cmdString);
    }
    
    public Reply getConf(String keyword) { // 3.3
    	TorCommand cmd = TorCommand.GETCONF;
    	String cmdString = cmd.getCommandString() + " " + keyword;
    	return sendMsg(cmdString);
    }
    
    public Reply setEvents(SetEvent... setEvents) { // 3.4
    	TorCommand cmd = TorCommand.SETEVENTS;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(SetEvent curSetEvent : setEvents) {
    		cmdString.append(" ");
    		cmdString.append(curSetEvent.getsetEventString());
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply setEvents(String... setEvents) { // 3.4
    	TorCommand cmd = TorCommand.SETEVENTS;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(String setEvent : setEvents) {
    		cmdString.append(" ");
    		cmdString.append(setEvent);
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply saveConf() { // 3.6
    	TorCommand cmd = TorCommand.SAVECONF;
    	String cmdString = cmd.getCommandString();
    	return sendMsg(cmdString);
    }
    
    public Reply signal(Signal... signals) { // 3.7
    	TorCommand cmd = TorCommand.SIGNAL;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(Signal signal : signals) {
    		cmdString.append(" ");
    		cmdString.append(signal.getSignalString());
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply signal(String... signals) { // 3.7
    	TorCommand cmd = TorCommand.SIGNAL;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(String signal : signals) {
    		cmdString.append(" ");
    		cmdString.append(signal);
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply mapAddress(String oldAddress, String newAddress) { // 3.8
    	TorCommand cmd = TorCommand.MAPADDRESS;
    	String cmdString = cmd.getCommandString() + " " + oldAddress + "=" + newAddress;
    	return sendMsg(cmdString);
    }
    
    public Reply getInfo(String keyword) { // 3.9  TODO Handle multiple keywords?.
    	TorCommand cmd = TorCommand.GETINFO;
    	String cmdString = cmd.getCommandString() + " " + keyword;
    	handler.setGetInfoMode();
    	return sendMsg(cmdString);
    }
    
//    public Reply extendCircuit() {
//    	
//    }
        
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