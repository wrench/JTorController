package com.mountainsofmars.jtorcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mountainsofmars.jtorcontroller.listenerevent.TorListenerEvent;

public class InfoMessageDispatcher extends Thread {
	private static List<InfoListener> listeners = new ArrayList<InfoListener>();
	private static Queue<String> messageQueue = new LinkedBlockingQueue<String>();
	
	static void queueMessage(String message) {
		messageQueue.add(message);
	}
	
	static void addListener(InfoListener listener) {
		listeners.add(listener);
	}
	
	public void run() {
		System.out.println("IMD RUNNING!");
		String message = null;
		while(true) {
			try {
				message = ((LinkedBlockingQueue<String>) messageQueue).take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(InfoListener listener : listeners) {
				listener.handleInfoMessage(message);
			}
		}
		
	}
}
