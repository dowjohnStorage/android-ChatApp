package com.epicodus.chatapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guest on 7/13/16.
 */
public class Message {
    private String user;
    private String message;
    private String pushId;

    public Message() {}

    public Message(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {return message;}

    public String getUser() {return user;}

    public void setPushId( String mPushId) {
        this.pushId = mPushId;
    }
}
