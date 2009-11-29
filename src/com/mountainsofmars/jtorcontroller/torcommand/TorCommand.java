package com.mountainsofmars.jtorcontroller.torcommand;

/**
 * Ben Tate
 * Date: Oct 12, 2009
 */
public enum TorCommand {
    AUTHENTICATE("AUTHENTICATE"), SETCONF("SETCONF"), GETCONF("GETCONF"), SETEVENTS("SETEVENTS"), SAVECONF("SAVECONF"), SIGNAL("SIGNAL"), MAPADDRESS("MAPADDRESS"), GETINFO("GETINFO");

    private String commandString;

    private TorCommand(String commandString) {
        this.commandString = commandString;
    }
    
    public String getCommandString() {
        return commandString;
    }
}
