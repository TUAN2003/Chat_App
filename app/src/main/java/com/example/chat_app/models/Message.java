package com.example.chat_app.models;

import java.util.Date;

public class Message {
    public String senderId, receiverId, message, dateTime;
    public Date dateObject;

    public Message(String senderId, String receiverId, String message, String dateTime, Date dateObject) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
        this.dateObject = dateObject;
    }
    public Message(){}
}
