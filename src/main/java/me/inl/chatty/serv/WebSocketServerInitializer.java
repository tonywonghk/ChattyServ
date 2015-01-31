package me.inl.chatty.serv;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * Created by Tony Wong on 31/1/15.
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel>{

    private final SslContext sslCtx;
    private final ChannelGroup group;


    public WebSocketServerInitializer(ChannelGroup group, SslContext sslCtx){
        this.sslCtx = sslCtx;
        this.group = group;
    }

    @Override
    public void initChannel(Channel ch) throws Exception{
        ChannelPipeline pipeline = ch.pipeline();
        if(sslCtx != null){
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        String contextPath = "/ws";

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpRequestHandler(contextPath));
        pipeline.addLast(new WebSocketServerProtocolHandler(contextPath));
        pipeline.addLast(new TextWebSocketFrameHandler(group));


    }
}
