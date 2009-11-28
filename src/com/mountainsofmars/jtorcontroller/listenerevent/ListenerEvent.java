package com.mountainsofmars.jtorcontroller.listenerevent;

public enum ListenerEvent {

	    ON_CONNECT("onConnect"), ON_DISCONNECT("onDisconnect");

	    private String methodName;

	    private ListenerEvent(String methodName) {
	        this.methodName = methodName;
	    }

	    public String getMethodName() {
	        return methodName;
	    }
	
}
