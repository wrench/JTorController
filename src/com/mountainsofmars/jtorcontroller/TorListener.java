package com.mountainsofmars.jtorcontroller;

public interface TorListener {

    public void onConnect();
    
    public void onDisconnect();
    
    public void handleInfoMessage(String message);
    
}
