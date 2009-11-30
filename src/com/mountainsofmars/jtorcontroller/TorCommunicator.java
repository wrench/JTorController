package com.mountainsofmars.jtorcontroller;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import java.util.concurrent.Executors;
import java.net.InetSocketAddress;

public class TorCommunicator implements Runnable {
    private static final Logger logger = Logger.getLogger(TorCommunicator.class);
    private TorProtocolHandler chandler;
    private String host;
    private int port;

    public TorCommunicator(TorProtocolHandler chandler, String host, int port) {
        this.chandler = chandler;
        this.host = host;
        this.port = port;
    }

    public void run() {
        // Configure ClientBootstrap.
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new TorPipelineFactory(chandler));
        logger.info("Connecting to TOR...");
       // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
       // Wait until either the connection is closed or the connection attempt fails.
        future.getChannel().getCloseFuture().awaitUninterruptibly();
       // Shut down thread pools and exit.
        bootstrap.releaseExternalResources();
    }
}
