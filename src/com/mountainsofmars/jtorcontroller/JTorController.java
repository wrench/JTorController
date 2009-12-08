package com.mountainsofmars.jtorcontroller;

import org.apache.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.mountainsofmars.jtorcontroller.purpose.Purpose;
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

    public Reply authenticate() { // 3.5 Authentication with no hashed password or cookie.
        TorCommand cmd = TorCommand.AUTHENTICATE;
        return sendMsg(cmd.getCommandString());
    }
    
    public Reply authenticate(String password) { // 3.5 Uses hashed password.
    	TorCommand cmd = TorCommand.AUTHENTICATE;
    	String cmdString = cmd.getCommandString() + " \"" + password + "\"";
    	return sendMsg(cmdString);
    } 
    
    public Reply authenticateCookiePassword() { // 3.5
    	//TODO Add method body. This does not work.
    	Reply reply = null;
    	return reply;
    }
    
    public Reply setConf(String keyword, String value) { // 3.1 Set one key/value pair.
    	TorCommand cmd = TorCommand.SETCONF; 
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
    
    public Reply setEvents(String... setEvents) { // 3.4 Uses string representation of set events.
    	TorCommand cmd = TorCommand.SETEVENTS;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(String setEvent : setEvents) {
    		cmdString.append(" ");
    		cmdString.append(setEvent);
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply setEvents(SetEvent... setEvents) { // 3.4 Uses SetEvent(enum) representation of set events.
    	TorCommand cmd = TorCommand.SETEVENTS;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(SetEvent curSetEvent : setEvents) {
    		cmdString.append(" ");
    		cmdString.append(curSetEvent.getsetEventString());
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply saveConf() { // 3.6
    	TorCommand cmd = TorCommand.SAVECONF;
    	String cmdString = cmd.getCommandString();
    	return sendMsg(cmdString);
    }
    
    public Reply signal(String... signals) { // 3.7 Uses string representation of signals.
    	TorCommand cmd = TorCommand.SIGNAL;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(String signal : signals) {
    		cmdString.append(" ");
    		cmdString.append(signal);
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply signal(Signal... signals) { // 3.7 Uses Signal(enum) representation of signals.
    	TorCommand cmd = TorCommand.SIGNAL;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(Signal signal : signals) {
    		cmdString.append(" ");
    		cmdString.append(signal.getSignalString());
    	}
    	return sendMsg(cmdString.toString());
    }
    
    public Reply mapAddress(String oldAddress, String newAddress) { // 3.8
    	TorCommand cmd = TorCommand.MAPADDRESS;
    	String cmdString = cmd.getCommandString() + " " + oldAddress + "=" + newAddress;
    	return sendMsg(cmdString);
    }
    
    public Reply getInfo(String... keywords) { // 3.9  
    	TorCommand cmd = TorCommand.GETINFO;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	for(String keyword : keywords) {
    		cmdString.append(" ");
    		cmdString.append(keyword);
    	}
    	handler.setGetInfoMode();
    	return sendMsg(cmdString.toString());
    }
    
    public Reply extendCircuit(int circuitID, String specifiedPath, String purpose) { // 3.10 Uses string representation of the purpose.
		TorCommand cmd = TorCommand.EXTENDCIRCUIT; //TODO Need to add support for multiple specified paths?
		StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
		cmdString.append(" ");
		cmdString.append(circuitID);
		cmdString.append(" ");
		cmdString.append(specifiedPath);
		cmdString.append(" ");
		cmdString.append("purpose=");
		cmdString.append(purpose);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply extendCircuit(int circuitID, String specifiedPath, Purpose purpose) { // 3.10 Uses the Purpose(enum) representation of the purpose.
		TorCommand cmd = TorCommand.EXTENDCIRCUIT;
		StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
		cmdString.append(" ");
		cmdString.append(circuitID);
		cmdString.append(" ");
		cmdString.append(specifiedPath);
		cmdString.append(" ");
		cmdString.append("purpose=");
		cmdString.append(purpose.getPurposeString());
    	return sendMsg(cmdString.toString());
    }
    
    public Reply extendCircuit(int circuitID, String specifiedPath) { // 3.10 Does not specify a purpose value.
		TorCommand cmd = TorCommand.EXTENDCIRCUIT;
		StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
		cmdString.append(" ");
		cmdString.append(circuitID);
		cmdString.append(" ");
		cmdString.append(specifiedPath);
		return sendMsg(cmdString.toString());
    }
    
    public Reply setCircuitPurpose(int circuitID, String specifiedPath, String purpose) { // 3.11 Uses string representation of the purpose.
    	TorCommand cmd = TorCommand.SETCIRCUITPURPOSE;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(specifiedPath);
    	cmdString.append(" ");
    	cmdString.append(purpose);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply setCircuitPurpose(int circuitID, String specifiedPath, Purpose purpose) { // 3.11 Uses Purpose(enum) representation of the purpose.
    	TorCommand cmd = TorCommand.SETCIRCUITPURPOSE;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(specifiedPath);
    	cmdString.append(" ");
    	cmdString.append(purpose.getPurposeString());
    	return sendMsg(cmdString.toString());
    }
    
    public Reply setRouterPurpose(String nickOrKey, String purpose) { // 3.12 Uses string representation of the purpose. This command is now DEPRECATED.
    	TorCommand cmd = TorCommand.SETROUTERPURPOSE;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(nickOrKey);
    	cmdString.append(" ");
    	cmdString.append(purpose);
    	return sendMsg(cmdString.toString());
    }
 
    public Reply setRouterPurpose(String nickOrKey, Purpose purpose) { // 3.12 Uses Purpose(enum) representation of the purpose. This command is now DEPRECATED.
    	TorCommand cmd = TorCommand.SETROUTERPURPOSE;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(nickOrKey);
    	cmdString.append(" ");
    	cmdString.append(purpose.getPurposeString());
    	return sendMsg(cmdString.toString());
    }
    
    public Reply attachStream(int streamID, int circuitID, int hopNum) { // 3.13 Uses a hopnum.
    	TorCommand cmd = TorCommand.ATTACHSTREAM;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(streamID);
    	cmdString.append(" ");
    	cmdString.append(circuitID);
    	cmdString.append(" ");
    	cmdString.append("HOP=");
    	cmdString.append(hopNum);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply attachStream(int streamID, int circuitID) { // 3.13 Does not use a hopnum.
    	TorCommand cmd = TorCommand.ATTACHSTREAM;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(streamID);
    	cmdString.append(" ");
    	cmdString.append(circuitID);
    	return sendMsg(cmdString.toString());
    }
    
    //public Reply PostDescriptor { // 3.14 No fucking clue about what a descriptor's supposed to look like.
    
    public Reply redirectStream(int streamID, String address, int port) { // 3.15 Uses a port number.
    	TorCommand cmd = TorCommand.REDIRECTSTREAM;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(streamID);
    	cmdString.append(" ");
    	cmdString.append(address);
    	cmdString.append(" ");
    	cmdString.append(port);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply redirectStream(int streamID, String address) { // 3.15 Does not use a port number.
    	TorCommand cmd = TorCommand.REDIRECTSTREAM;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(streamID);
    	cmdString.append(" ");
    	cmdString.append(address);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply closeStream(int streamID, int reason) { // 3.16
    	TorCommand cmd = TorCommand.CLOSESTREAM;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(streamID);
    	cmdString.append(" ");
    	cmdString.append(reason);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply closeCircuit(int streamID, String flag) { // 3.17
    	TorCommand cmd = TorCommand.CLOSECIRCUIT;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(streamID);
    	cmdString.append(" ");
    	cmdString.append(flag);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply quit() { // 3.18 //TODO Make this terminate the entire program?
    	TorCommand cmd = TorCommand.QUIT;
    	return sendMsg(cmd.getCommandString());
    }
    
    public Reply useFeature(String featureName) { // 3.19
    	TorCommand cmd = TorCommand.USEFEATURE;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(featureName);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply resolve(String option, String address) { // 3.20
    	TorCommand cmd = TorCommand.RESOLVE;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(option);
    	cmdString.append(" ");
    	cmdString.append(address);
    	return sendMsg(cmdString.toString());
    }
    
    public Reply protocolInfo(int piVersion) { // 3.21
    	TorCommand cmd = TorCommand.PROTOCOLINFO;
    	StringBuilder cmdString = new StringBuilder(cmd.getCommandString());
    	cmdString.append(" ");
    	cmdString.append(piVersion);
    	return sendMsg(cmdString.toString());
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