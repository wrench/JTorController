package com.mountainsofmars.jtorcontroller;

import com.mountainsofmars.jtorcontroller.reply.Reply;

/**
 * Ben Tate
 * Date: Oct 13, 2009
 */
public class Application implements TorListener {

    JTorController jtc;

    public static void main(String[] args) {
        Application app = new Application();
    }

    public Application() {
        jtc = new JTorController("127.0.0.1", 9051, this);
        jtc.connect();
    }

    public void onConnect() {
        Reply reply = jtc.authenticateNoPassword();
        System.out.println("Reply msg: " + reply.getMessage());
    }

    public void onDisconnect() {
        System.out.println("not connected");
    }

}