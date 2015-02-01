package me.inl.chatty.model;

import com.google.gson.Gson;

/**
 * Created by Tony Wong on 31/1/15.
 */
public class ServiceResponse {

    public static final int StatusReceived = 200;
    public static final int StatusSuccess = 202;
    public static final int StatusUnauthorized = 401;
    public static final int StatusUnknownError = 404;

    private int type;
    private int action;
    private int status;
    private String payload;
    private String datetime;

    public ServiceResponse(){

    }

    public ServiceResponse(ServiceRequest sr){

        setType(sr.getType());
        setAction(sr.getAction());
    }

    public String toJson(){
        Gson gson = new Gson();
        String result = gson.toJson(this);
        return result;
    }

    public static ServiceResponse FromJson(String json){
        Gson gson = new Gson();
        ServiceResponse sr = gson.fromJson(json, ServiceResponse.class);
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString(){
        return "ServiceResponse [type="+type+", action="+ action+", status="+status+", payload="+ payload+"]";
    }
}
