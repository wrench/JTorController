package com.mountainsofmars.jtorcontroller.listenerevent;

public class TorListenerEvent {
	private TorListenerEventType type;
	private String infoMessage;
	
	public TorListenerEvent(TorListenerEventType type) {
		this.type = type;
	}
	
	public TorListenerEvent(TorListenerEventType type, String infoMessage) {
		this.type = type;
		this.infoMessage = infoMessage;
	}
	
	public TorListenerEventType getType() {
		return type;
	}
	
	public String getInfoMessage() {
		return infoMessage;
	}
}
