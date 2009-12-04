package com.mountainsofmars.jtorcontroller.reply;

import java.util.ArrayList;
import java.util.List;

public class ReplyImpl implements Reply {

    private List<String> messages;
    private boolean multilineMessage;

    protected ReplyImpl(String message) {
        this.messages = new ArrayList<String>();
        messages.add(message);
        this.multilineMessage = false;
    }
    
    protected ReplyImpl(List<String> messages) {
    	this.messages = messages;
    	this.multilineMessage = true;
    }
    
    public String getMessage() {
        return messages.get(0);
    }
    
    public List<String> getMessages() {
    	return messages;
    }
    
    public boolean isMultilineMessage() {
    	return multilineMessage;
    }
}
