package com.mountainsofmars.jtorcontroller;

/**
 * Ben Tate
 * Date: Oct 16, 2009
 */
public interface TorListener {

    public void onConnect();
    
    public void onDisconnect();
    
    public void sendEvent(String event);
}
