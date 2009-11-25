package com.mountainsofmars.jtorcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import com.mountainsofmars.jtorcontroller.event.Event;

public class EventDispatcher extends Thread {
	private static List<TorListener> listeners = new ArrayList<TorListener>();
	private static Queue<Event> eventQueue = new LinkedBlockingQueue<Event>();

	static void fireEvent(Event evt) {
		eventQueue.add(evt);
	}
	
	static void addListener(TorListener listener) {
		listeners.add(listener);
	}
	
	public void run() {
		Event evt = null;
		while(true) {
			try {
				evt = ((LinkedBlockingQueue<Event>) eventQueue).take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(TorListener tl : listeners) {
				if(evt.equals(Event.ON_CONNECT)) {
					tl.onConnect();
				} else if(evt.equals(Event.ON_DISCONNECT)) {
					tl.onDisconnect();
				}
			}
		}
		
	}
}
