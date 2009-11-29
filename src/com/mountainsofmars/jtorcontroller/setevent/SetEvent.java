package com.mountainsofmars.jtorcontroller.setevent;

public enum SetEvent {
	CIRC("CIRC"), STREAM("STREAM"), ORCONN("ORCONN"), BW("BW"), DEBUG("DEBUG"), INFO("INFO"), NOTICE("NOTICE"), WARN("WARN"), ERR("ERR"),
		NEWDESC("NEWDESC"), ADDRMAP("ADDRMAP"), AUTHDIR_NEWDESCS("AUTHDIR_NEWDESCS"), DESCCHANGED("DESCCHANGED"), STATUS_GENERAL("STATUS_GENERAL"), 
		STATUS_CLIENT("STATUS_CLIENT"), STATUS_SERVER("STATUS_SERVER"), GUARD("GUARD"), NS("NS"), STREAM_BW("STREAM_BW"), CLIENTS_SEEN("CLIENTS_SEEN"), 
		NEWCONSENSUS("NEWCONSENSUS");
	
	private String setEventString;
	
	 private SetEvent(String setEventString) {
	        this.setEventString = setEventString;
	    }
	    
	    public String getsetEventString() {
	        return setEventString;
	    }
}
