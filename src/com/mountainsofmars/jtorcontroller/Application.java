package com.mountainsofmars.jtorcontroller;

import com.mountainsofmars.jtorcontroller.reply.Reply;
import com.mountainsofmars.jtorcontroller.reply.SuccessReply;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
public class Application implements TorListener {

    private JTorController jtc;

    public static void main(String[] args) throws Exception {
        Application app = new Application();        
    }
    
    public Application() {
        jtc = new JTorController("127.0.0.1", 9051, this);
        jtc.connect();  
    }

    public void onConnect() {
    	//Reply reply = jtc.authenticateNoPassword();
    	Reply reply = jtc.authenticateHashedPassword("secret");
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
    	//Reply reply = jtc.setEvents("CIRC", "STREAM", "ORCONN");
    	//Reply reply = jtc.saveConf();
    	Reply reply = jtc.signal("RELOAD");
    	System.out.println("Reply msg: " + reply.getMessage());
    	
    }
    
    public void onAuthenticationFailure() {
    	System.out.println("Authentication Failed!");
    	onDisconnect(); // Tor disconnects clients following a failed authentication attempt.
    }

    public void onDisconnect() {
        System.out.println("Disconnected from Tor");
    }

}