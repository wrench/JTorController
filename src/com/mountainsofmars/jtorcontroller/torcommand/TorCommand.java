package com.mountainsofmars.jtorcontroller.torcommand;

public enum TorCommand {
    AUTHENTICATE("AUTHENTICATE"), SETCONF("SETCONF"), RESETCONF("RESETCONF"), GETCONF("GETCONF"), SETEVENTS("SETEVENTS"), 
    	SAVECONF("SAVECONF"), SIGNAL("SIGNAL"), MAPADDRESS("MAPADDRESS"), GETINFO("GETINFO"), EXTENDCIRCUIT("EXTENDCIRCUIT"), SETCIRCUITPURPOSE("SETCIRCUITPURPOSE"),
    	SETROUTERPURPOSE("SETROUTERPURPOSE"), ATTACHSTREAM("ATTACHSTREAM"), REDIRECTSTREAM("REDIRECTSTREAM"), CLOSESTREAM("CLOSESTREAM"), CLOSECIRCUIT("CLOSECIRCUIT"), 
    	QUIT("QUIT"), USEFEATURE("USEFEATURE"), RESOLVE("RESOLVE"), PROTOCOLINFO("PROTOCOLINFO");

    private String commandString;

    private TorCommand(String commandString) {
        this.commandString = commandString;
    }
    
    public String getCommandString() {
        return commandString;
    }
}
