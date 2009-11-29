package com.mountainsofmars.jtorcontroller.signal;

public enum Signal {
	 RELOAD("RELOAD"), SHUTDOWN("SHUTDOWN"), DUMP("DUMP"), DEBUG("DEBUG"), HALT("HALT"), HUP("HUP"), INT("INT"), 
	 					USR1("USR1"), USR2("USR2"), TERM("TERM"), NEWNYM("NEWNYM"), CLEARDNSCACHE("CLEARDNSCACHE");

	    private String signalString;

	    private Signal(String signalString) {
	        this.signalString = signalString;
	    }
	    
	    public String getSignalString() {
	        return signalString;
	    }
}
