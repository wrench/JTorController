package com.mountainsofmars.jtorcontroller.command;

/**
 * Ben Tate
 * Date: Oct 12, 2009
 */
public enum Command {
    AUTHENTICATE("AUTHENTICATE"), SETCONF("SETCONF"), GETCONF("GETCONF"), SETEVENTS("SETEVENTS"), SAVECONF("SAVECONF"), SIGNAL("SIGNAL"), MAPADDRESS("MAPADDRESS"), GETINFO("GETINFO");

    private String commandString;

    private Command(String commandString) {
        this.commandString = commandString;
    }
    
    private Command(String commandString, String password) {
    	this.commandString = commandString + " \"" + password + "\"";
    }

    public String getCommandString() {
        return commandString;
    }
}
