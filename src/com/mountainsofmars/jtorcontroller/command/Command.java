package com.mountainsofmars.jtorcontroller.command;

/**
 * Ben Tate
 * Date: Oct 12, 2009
 */
public enum Command {
    AUTHENTICATE_NO_PASSWORD("AUTHENTICATE");

    private String commandString;

    private Command(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
