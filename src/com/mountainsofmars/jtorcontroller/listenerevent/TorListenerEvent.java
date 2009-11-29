package com.mountainsofmars.jtorcontroller.listenerevent;

public enum TorListenerEvent {

	    ON_CONNECT("onConnect"), ON_DISCONNECT("onDisconnect");

	    private String methodName;

	    private TorListenerEvent(String methodName) {
	        this.methodName = methodName;
	    }

	    public String getMethodName() {
	        return methodName;
	    }
	
}
