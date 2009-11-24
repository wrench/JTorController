package com.mountainsofmars.jtorcontroller;

import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import com.mountainsofmars.jtorcontroller.event.Event;
import com.mountainsofmars.jtorcontroller.reply.Reply;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
public class Application implements TorListener {

    private JTorController jtc;
    private Queue<Event> eventQueue;

    public static void main(String[] args) throws Exception {
        Application app = new Application();        
        app.listenForEvents();
    }
    
    public Application() {
        jtc = new JTorController("127.0.0.1", 9051, this);
        jtc.connect();
        eventQueue = new LinkedBlockingQueue<Event>();
    }
    
    private void listenForEvents() throws Exception {
    	Event evt = ((LinkedBlockingQueue<Event>) eventQueue).take();
    	Method method = this.getClass().getMethod(evt.getMethodName());
    	method.invoke(this);
    }
    
    public void sendEvent(Event evt) {
    	eventQueue.add(evt);
    }

    public void onConnect() {
    	Reply reply = jtc.authenticateNoPassword();
        System.out.println("Reply msg: " + reply.getMessage());
    }

    public void onDisconnect() {
        System.out.println("not connected");
    }

}