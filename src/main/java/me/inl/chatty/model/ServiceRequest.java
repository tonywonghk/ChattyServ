package me.inl.chatty.model;

/**
 * Created by cah7k2w on 31/1/15.
 */
public class ServiceRequest {

    private String type;
    private String action;
    private String payload;

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

    @Override
    public String toString(){
        return "ServiceRequest [type =" + type + ", action=" + action + ", payload={" + payload + "}]" ;
    }
}
