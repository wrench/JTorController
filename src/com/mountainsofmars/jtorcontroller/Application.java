package com.mountainsofmars.jtorcontroller;

import com.mountainsofmars.jtorcontroller.reply.Reply;
import com.mountainsofmars.jtorcontroller.reply.SuccessReply;
import com.mountainsofmars.jtorcontroller.setevent.SetEvent;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
public class Application implements TorListener, InfoListener {

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
    	Reply reply = jtc.setEvents(this, SetEvent.CIRC, SetEvent.STREAM, SetEvent.ORCONN, SetEvent.BW, SetEvent.DEBUG, SetEvent.INFO, SetEvent.NOTICE, SetEvent.WARN, SetEvent.ERR, SetEvent.NEWDESC, SetEvent.ADDRMAP, SetEvent.AUTHDIR_NEWDESCS, SetEvent.DESCCHANGED, SetEvent.STATUS_GENERAL, SetEvent.STATUS_CLIENT, SetEvent.STATUS_SERVER, SetEvent.GUARD, SetEvent.NS, SetEvent.STREAM_BW, SetEvent.CLIENTS_SEEN, SetEvent.NEWCONSENSUS);
    	//Reply reply = jtc.saveConf();
    	//Reply reply = jtc.signal("RELOAD");
    	//Reply reply = jtc.getInfo("config-file");
    	//Reply reply = jtc.getInfo("version");
    	System.out.println("Reply msg: " + reply.getMessage());
    	
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

}