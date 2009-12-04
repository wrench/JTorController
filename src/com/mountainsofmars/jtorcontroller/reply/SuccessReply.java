package com.mountainsofmars.jtorcontroller.reply;

import java.util.List;

public class SuccessReply extends ReplyImpl {

    public SuccessReply(String message) {
        super(message);
    }
    
    public SuccessReply(List<String> messages) {
    	super(messages);
    }
}
