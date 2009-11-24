package com.mountainsofmars.jtorcontroller;

import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import com.mountainsofmars.jtorcontroller.reply.Reply;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
public class Application implements TorListener {

    private JTorController jtc;
    private Queue<String> eventQueue;

    public static void main(String[] args) throws Exception {
        Application app = new Application();        
        app.listenForEvents();
    }
    
    public Application() {
        jtc = new JTorController("127.0.0.1", 9051, this);
        jtc.connect();
        eventQueue = new LinkedBlockingQueue<String>();
    }
    
    private void listenForEvents() throws Exception {
    	String methodName = ((LinkedBlockingQueue<String>) eventQueue).take();
    	Method method = this.getClass().getMethod(methodName);
    	method.invoke(this);
    }
    
    public void sendEvent(String evt) {
    	eventQueue.add(evt);
    }

    public void onConnect() {
    	System.out.println("Got inside onConnect method...");
        Reply reply = jtc.authenticateNoPassword();
        System.out.println("Reply msg: " + reply.getMessage());
    }

    public void onDisconnect() {
        System.out.println("not connected");
    }

}