package com.mountainsofmars.jtorcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mountainsofmars.jtorcontroller.listenerevent.ListenerEvent;

public class EventDispatcher extends Thread {
	private static List<TorListener> listeners = new ArrayList<TorListener>();
	private static Queue<ListenerEvent> eventQueue = new LinkedBlockingQueue<ListenerEvent>();

	static void fireEvent(ListenerEvent evt) {
		eventQueue.add(evt);
	}
	
	static void addListener(TorListener listener) {
		listeners.add(listener);
	}
	
	public void run() {
		ListenerEvent evt = null;
		while(true) {
			try {
				evt = ((LinkedBlockingQueue<ListenerEvent>) eventQueue).take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(TorListener tl : listeners) {
				if(evt.equals(ListenerEvent.ON_CONNECT)) {
					tl.onConnect();
				} else if(evt.equals(ListenerEvent.ON_DISCONNECT)) {
					tl.onDisconnect();
				}
			}
		}
		
	}
}
