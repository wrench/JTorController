package com.mountainsofmars.jtorcontroller.purpose;

public enum Purpose {
	GENERAL("general"), CONTROLLER("controller"), BRIDGE("bridge");
	
	private String purposeString;

    private Purpose(String purposeString) {
        this.purposeString = purposeString;
    }
    
    public String getPurposeString() {
        return purposeString;
    }
}
