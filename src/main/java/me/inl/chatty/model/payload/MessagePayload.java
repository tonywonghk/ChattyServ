package me.inl.chatty.model.payload;

import com.google.gson.Gson;

/**
 * Created by Tony Wong on 31/1/15.
 */
public class MessagePayload {

    private String fromUID;
    private String toUID;
    private String type;
    private String text;
    private String url;

    public String toJson(){
        Gson gson = new Gson();
        String result = gson.toJson(this);
        return result;
    }

    public static MessagePayload FromJson(String json){
        Gson gson = new Gson();
        MessagePayload payload = gson.fromJson(json, MessagePayload.class);
        return payload;
    }


    public String getFromUID() {
        return fromUID;
    }

    public void setFromUID(String fromUID) {
        this.fromUID = fromUID;
    }

    public String getToUID() {
        return toUID;
    }

    public void setToUID(String toUID) {
        this.toUID = toUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
