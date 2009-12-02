package com.mountainsofmars.jtorcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import com.mountainsofmars.jtorcontroller.listenerevent.TorListenerEvent;

public class EventDispatcher extends Thread {
	private static List<TorListener> listeners = new ArrayList<TorListener>();
	private static Queue<TorListenerEvent> eventQueue = new LinkedBlockingQueue<TorListenerEvent>();
	
	static void fireEvent(TorListenerEvent evt) {
		eventQueue.add(evt);
	}
	
	static void addListener(TorListener listener) {
		listeners.add(listener);
	}
	
	public void run() {
		TorListenerEvent evt = null;
		while(true) {
			try {
				evt = ((LinkedBlockingQueue<TorListenerEvent>) eventQueue).take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(TorListener tl : listeners) {
				switch(evt.getType()) {
				case ON_CONNECT:
					tl.onConnect();
					break;
				case ON_DISCONNECT:
					tl.onDisconnect();
					break;
				case INFO_MESSAGE:
					tl.handleInfoMessage(evt.getInfoMessage());
					break;
				}
			}
		}
		
	}
}
