package me.inl.chatty.model;

import com.google.gson.Gson;

/**
 * Created by cah7k2w on 31/1/15.
 */
public class ServiceSession {
    private String token;
    private String device;
    private String sourceIP;


    public String getToken() {
        return token;
    }

    public static ServiceSession FromJson(String json){
        Gson gson = new Gson();
        ServiceSession session = gson.fromJson(json, ServiceSession.class);
        return session;
    }

    public static String ToJson(ServiceSession session){
        Gson gson = new Gson();
        String result = gson.toJson(session);
        return result;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }
}
