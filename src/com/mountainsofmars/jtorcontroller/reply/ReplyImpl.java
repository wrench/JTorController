package com.mountainsofmars.jtorcontroller.reply;

public class ReplyImpl implements Reply {

    private String message;

    protected ReplyImpl(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
