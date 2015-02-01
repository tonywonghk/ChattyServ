package me.inl.chatty.serv;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.ImmediateEventExecutor;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tony Wong on 31/1/15.
 */
public class ChattyServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    private final SslContext sslCtx;
    private final Jedis jedis;

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    private Map<String, ChannelHandlerContext> connHash = new HashMap<String, ChannelHandlerContext>(2048);

    public ChattyServer(SslContext sslCtx, Jedis jedis){
        this.sslCtx = sslCtx;
        this.jedis = jedis;
    }

    public ChannelFuture start(InetSocketAddress address){
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(createInitializer(channelGroup, connHash));

        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();

        System.out.println("ChattyServer is ready, you may open web browser and navigate to "
                + (SSL ? "https" : "http") + "://"+address.getAddress()+":" + address.getPort() + "/");

        return future;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group, Map<String, ChannelHandlerContext> connHash) {
        return new WebSocketServerInitializer(group,connHash,sslCtx,jedis);
    }

    public void destory(){
        if(channel != null){
            channel.close();
        }

        channelGroup.close();
        group.shutdownGracefully();
        System.out.println("Chatty Server is going down");
    }


    /**
     * @param args Server start up parameters. If the parameter is empty,
     *             server bind port 8333
     */
    public static void main(String[] args) throws Exception{
        System.out.println("Chatty Server is going to start");

        int serverPort = 8333;
        final SslContext sslCtx;
        final Jedis jedis;
        final String redis_host = "192.168.1.80";

        if(args != null & args.length > 0){
            serverPort = Integer.parseInt(args[0]);
        }

        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }

        jedis = new Jedis(redis_host);

        final ChattyServer endpoint = new ChattyServer(sslCtx, jedis);
        ChannelFuture future = endpoint.start(new InetSocketAddress(serverPort));
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run(){
                endpoint.destory();
            }
        });

        future.channel().closeFuture().syncUninterruptibly();
    }


}
