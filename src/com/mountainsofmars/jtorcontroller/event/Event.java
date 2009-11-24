package com.mountainsofmars.jtorcontroller.event;

public enum Event {

	
	    ON_CONNECT("onConnect"), ON_DISCONNECT("onDisconnect");

	    private String methodName;

	    private Event(String methodName) {
	        this.methodName = methodName;
	    }

	    public String getMethodName() {
	        return methodName;
	    }
	
}
