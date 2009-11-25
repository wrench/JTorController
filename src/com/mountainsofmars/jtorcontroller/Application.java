package com.mountainsofmars.jtorcontroller;

import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import com.mountainsofmars.jtorcontroller.event.Event;
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
    	Reply reply = jtc.setConf("Nickname", "wrench");
    	System.out.println("Reply msg: " + reply.getMessage());
    	reply = jtc.getConf("Nickname");
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