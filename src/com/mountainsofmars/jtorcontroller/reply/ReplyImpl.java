package com.mountainsofmars.jtorcontroller.reply;

/**
 * Ben Tate
 * Date: Oct 16, 2009
 */
public class ReplyImpl implements Reply {

    private String message;

    protected ReplyImpl(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
