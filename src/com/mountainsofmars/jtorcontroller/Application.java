package com.mountainsofmars.jtorcontroller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.mountainsofmars.jtorcontroller.reply.Reply;
import com.mountainsofmars.jtorcontroller.reply.SuccessReply;
import com.mountainsofmars.jtorcontroller.setevent.SetEvent;

public class Application implements TorListener {

    private JTorController jtc;

    public static void main(String[] args) throws Exception {
        Application app = new Application();        
    }
    
    public Application() {
        jtc = new JTorController("127.0.0.1", 9051, this);
        jtc.connect();  
    }

    @Override
    public void onConnect() {
    	//Reply reply = jtc.authenticate();
    	Reply reply = jtc.authenticate("secret");
        System.out.println("Reply msg: " + reply.getMessage());
    	if(reply instanceof SuccessReply) {
    		onAuthenticationSuccess();
    	} else {
    		onAuthenticationFailure();
    	}
    }
    
    public void onAuthenticationSuccess() {
    	System.out.println("Authentication Succeeded!");
    	//Reply reply = jtc.setConf("Nickname", "wrench");
    	//Reply reply = jtc.setConf("MaxCircuitDirtiness", "100");
    	//System.out.println("Reply msg: " + reply.getMessage());
    	//reply = jtc.getConf("Nickname");
    	//reply = jtc.getConf("MaxCircuitDirtiness");
    	//System.out.println("Reply msg: " + reply.getMessage());
    	//Reply reply = jtc.setEvents(this, "CIRC", "STREAM", "ORCONN");
    	//Reply reply = jtc.setEvents(this, SetEvent.INFO, SetEvent.CIRC, SetEvent.BW);
    	//Reply reply0 = jtc.setEvents(SetEvent.CIRC, SetEvent.STREAM, SetEvent.ORCONN, SetEvent.BW, SetEvent.DEBUG, SetEvent.INFO, SetEvent.NOTICE, SetEvent.WARN, SetEvent.ERR, SetEvent.NEWDESC, SetEvent.ADDRMAP, SetEvent.AUTHDIR_NEWDESCS, SetEvent.DESCCHANGED, SetEvent.STATUS_GENERAL, SetEvent.STATUS_CLIENT, SetEvent.STATUS_SERVER, SetEvent.GUARD, SetEvent.NS, SetEvent.STREAM_BW, SetEvent.CLIENTS_SEEN, SetEvent.NEWCONSENSUS);
    	//Reply reply = jtc.saveConf();
    	//Reply reply = jtc.signal("RELOAD");
    	//Reply reply = jtc.signal(Signal.NEWNYM, Signal.DEBUG, Signal.CLEARDNSCACHE, Signal.DUMP, Signal.INT, Signal.RELOAD, Signal.TERM, Signal.USR1, Signal.USR2);
    	//Reply reply = jtc.getInfo("config-file");
    	//Reply reply = jtc.getInfo("desc/name/moria1");
    	//Reply reply = jtc.getInfo("network-status");
    	//Reply reply = jtc.getInfo("address-mappings/all");
//    	reply = jtc.resetConf("Nickname");
    	//System.out.println("Reply msg: " + reply.getMessage());
//    	reply = jtc.getConf("Nickname");
    	//reply = jtc.getConf("MaxCircuitDirtiness");
    	//Reply reply = jtc.mapAddress("1273232.192.10.10", "torproject.org");
    	Reply reply = jtc.extendCircuit(0, "www.tor.org", "general");
    	System.out.println("Reply msg: " + reply.getMessage());
    	writeToFile(reply.getMessages());
    }
    
    public void onAuthenticationFailure() {
    	System.out.println("Authentication Failed!");
    	onDisconnect(); // Tor disconnects clients following a failed authentication attempt.
    }
    
    @Override
    public void onDisconnect() {
        System.out.println("Disconnected from Tor");
    }

	@Override
	public void handleInfoMessage(String message) {
		System.out.println("INFO_LISTENER: " + message);
	}
	
	private void writeToFile(List<String> messages) {
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter("/sandbox/jtclog.txt"));
			for(String s : messages) {
				out.write(s);
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}