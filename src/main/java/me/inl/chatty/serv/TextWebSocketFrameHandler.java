package me.inl.chatty.serv;


import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import me.inl.chatty.model.ServiceRequest;
import me.inl.chatty.model.ServiceResponse;
import me.inl.chatty.model.ServiceSession;
import me.inl.chatty.model.payload.AuthPayload;
import me.inl.chatty.model.payload.MessagePayload;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Tony Wong on 31/1/15.
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    private final ChannelGroup group;
    private final Map<String, ChannelHandlerContext> connHash;
    private final Jedis jedis;
    private static final Logger log = Logger.getLogger(TextWebSocketFrameHandler.class.getName());


    public TextWebSocketFrameHandler(ChannelGroup group,Map<String, ChannelHandlerContext> connHash,Jedis jedis) {
        this.group = group;
        this.connHash = connHash;
        this.jedis = jedis;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);

//            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
//            group.add(ctx.channel());

        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        Gson gson = new Gson();
        ServiceRequest sr = gson.fromJson(msg.text(), ServiceRequest.class);

        if(sr != null){
            if(sr.getType() == ServiceRequest.RequestTypeAuth
                    && sr.getAction() == ServiceRequest.ActionLoginServ){

                TextWebSocketFrame frame;
                ServiceResponse response = new ServiceResponse(sr);

                try{
                    // process login process and return token
                    AuthPayload payload = AuthPayload.FromJson(sr.getPayload());
                    String keyValue = jedis.get(payload.getUid());
                    ServiceSession session = ServiceSession.FromJson(keyValue);


                    if (session.getToken().equals(payload.getToken())){
                        response.setStatus(ServiceResponse.StatusSuccess);

                        AuthPayload authPayload = new AuthPayload();
                        authPayload.setUid(payload.getUid());
                        authPayload.setToken(payload.getToken());
                        response.setPayload(authPayload.toJson());

                        // add client connection to group
                        group.add(ctx.channel());
                        connHash.put(payload.getUid(), ctx);
                    }else{
                        response.setStatus(ServiceResponse.StatusUnauthorized);
                    }

                }catch (Exception e){
                    response.setStatus(ServiceResponse.StatusUnknownError);
                    Map<String, String> error = new HashMap<String, String>();
                    error.put("error", e.getMessage());
                    response.setPayload(gson.toJson(error));
                }

                frame = new TextWebSocketFrame(response.toJson());
                ctx.writeAndFlush(frame);

            }else if(sr.getType() == ServiceRequest.RequestTypeMessage){

                if(sr.getAction() == ServiceRequest.ActionSendText
                        || sr.getAction() == ServiceRequest.ActionSendPicture
                        || sr.getAction() == ServiceRequest.ActionSendURL
                        || sr.getAction() == ServiceRequest.ActionSendLocation){

                    try{
                        MessagePayload payload = MessagePayload.FromJson(sr.getPayload());
                        ChannelHandlerContext toCtx = connHash.get(payload.getToUID());

                        ServiceResponse serviceResponse = new ServiceResponse(sr);
                        if(toCtx != null && toCtx.isRemoved() != true){
                            serviceResponse.setStatus(ServiceResponse.StatusSuccess);
                            serviceResponse.setPayload(sr.getPayload());

                            toCtx.writeAndFlush(new TextWebSocketFrame(serviceResponse.toJson()));
                            ctx.writeAndFlush(new TextWebSocketFrame(serviceResponse.toJson()));
                        }else{
                            //remove connection from the hash map
                            if( toCtx != null && toCtx.isRemoved()){
                                connHash.remove(payload.getToUID());
                            }

                            //user is offline, notifier sender the message is delivered
                            serviceResponse.setStatus(ServiceResponse.StatusReceived);
                            serviceResponse.setPayload(sr.getPayload());
                            ctx.writeAndFlush(new TextWebSocketFrame(serviceResponse.toJson()));
                        }

                    }catch (Exception e){
                        ServiceResponse errorResposne = new ServiceResponse(sr);
                        Map<String, String> errorMap = new HashMap<String, String>();
                        errorMap.put("error", e.getMessage());
                        errorResposne.setPayload(gson.toJson(errorMap));

                        ctx.writeAndFlush(new TextWebSocketFrame(errorResposne.toJson()));
                    }
                }
            }
        }

    }

}
