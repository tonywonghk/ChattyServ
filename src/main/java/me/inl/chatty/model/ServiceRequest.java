package me.inl.chatty.model;

import com.google.gson.Gson;

/**
 * Created by Tony Wong on 31/1/15.
 */
public class ServiceRequest {

    public static final int RequestTypeAuth = 1;
    public static final int ActionLoginServ = 1;

    public static final int RequestTypeMessage = 2;
    public static final int ActionSendText = 1;
    public static final int ActionSendPicture = 2;
    public static final int ActionSendURL = 3;
    public static final int ActionSendLocation = 4;
    public static final int ActionSendSound = 5;
    public static final int ActionSendVideo = 5;

    public static final int ActionJoinRoom = 1;




    private int type;
    private int action;
    private String payload;
    private String token;
    private String datetime;

    public ServiceRequest(){

    }

    public static String ToJson(ServiceRequest sr){
        Gson gson = new Gson();
        String result = gson.toJson(sr);
        return result;
    }

    public static ServiceRequest FromJson(String json){
        Gson gson = new Gson();
        ServiceRequest sr = gson.fromJson(json, ServiceRequest.class);
        return sr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString(){
        return "ServiceRequest [type =" + type + ", action=" + action + ", payload={" + payload + "}]" ;
    }


}
