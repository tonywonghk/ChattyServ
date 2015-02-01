package me.inl.chatty.model.payload;

import com.google.gson.Gson;

/**
 * Created by Tony Wong on 31/1/15.
 */
public class AuthPayload {
    private String uid;
    private String token;

    public  String toJson(){
        Gson gson = new Gson();
        String result = gson.toJson(this);
        return result;
    }

    public static AuthPayload FromJson(String json){
        Gson gson = new Gson();
        AuthPayload payload = gson.fromJson(json, AuthPayload.class);
        return payload;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
