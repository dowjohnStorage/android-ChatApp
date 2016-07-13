package com.epicodus.chatapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guest on 7/13/16.
 */
public class User {
    private String email;
    private List<Message> messages= new ArrayList<>();
    private String pushId;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
