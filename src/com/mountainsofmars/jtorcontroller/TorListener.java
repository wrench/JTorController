package com.mountainsofmars.jtorcontroller;

import com.mountainsofmars.jtorcontroller.listenerevent.TorListenerEvent;

/**
 * Ben Tate
 * Date: Oct 16, 2009
 */
public interface TorListener {

    public void onConnect();
    
    public void onDisconnect();
    
}
