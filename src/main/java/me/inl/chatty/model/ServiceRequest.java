package me.inl.chatty.model;

/**
 * Created by Tony Wong on 31/1/15.
 */
public class ServiceRequest {

    private String type;
    private String action;
    private String payload;
    private String token;
    private String datetime;

    public ServiceRequest(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
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
