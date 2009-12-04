package com.mountainsofmars.jtorcontroller.reply;

import java.util.List;

public interface Reply {

    String getMessage();
    
    List<String> getMessages();
    
    boolean isMultilineMessage();
  
}
