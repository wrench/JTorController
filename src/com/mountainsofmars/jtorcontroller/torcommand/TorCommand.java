package com.mountainsofmars.jtorcontroller.torcommand;

public enum TorCommand {
    AUTHENTICATE("AUTHENTICATE"), SETCONF("SETCONF"), RESETCONF("RESETCONF"), GETCONF("GETCONF"), SETEVENTS("SETEVENTS"), 
    	SAVECONF("SAVECONF"), SIGNAL("SIGNAL"), MAPADDRESS("MAPADDRESS"), GETINFO("GETINFO"), EXTENDCIRCUIT("EXTENDCIRCUIT");

    private String commandString;

    private TorCommand(String commandString) {
        this.commandString = commandString;
    }
    
    public String getCommandString() {
        return commandString;
    }
}
