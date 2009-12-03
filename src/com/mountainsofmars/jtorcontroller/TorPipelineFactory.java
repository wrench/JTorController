package com.mountainsofmars.jtorcontroller;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import static org.jboss.netty.channel.Channels.*;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class TorPipelineFactory implements org.jboss.netty.channel.ChannelPipelineFactory {

    private final ChannelHandler handler;

    public TorPipelineFactory(ChannelHandler handler) {
        this.handler = handler;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
//                8192, Delimiters.lineDelimiter()
//        ));
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
              90000, Delimiters.lineDelimiter()
      ));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", handler);
        return pipeline;
    }
}
