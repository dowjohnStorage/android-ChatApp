package com.epicodus.chatapp.models;

/**
 * Created by Guest on 7/13/16.
 */
public class Message {
    private String message;
    private String username;
    private String timestamp;
    private String pushId;

    public Message() {}

    public Message(String message, String username, String timestamp) {
        this.message = message;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getMessage() {return message;}

    public String getUsername() {return username;}

    public String getTimestamp() {return timestamp;}

    public void setPushId( String mPushId) {
        this.pushId = mPushId;
    }
}
