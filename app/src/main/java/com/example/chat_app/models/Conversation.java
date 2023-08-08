package com.example.chat_app.models;

import java.util.Date;

public class Conversation {
    public String conversationId;
    public String receiverId,receiverName,receiverImage;
    public String lastMessage,newMessageOf;
    public Date timestamp;
    public boolean status = false;
    public Conversation(){}

    public Conversation(String conversationId, String receiverId, String receiverName, String receiverImage, String lastMessage, String newMessageOf, Date timestamp, boolean status) {
        this.conversationId = conversationId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverImage = receiverImage;
        this.lastMessage = lastMessage;
        this.newMessageOf = newMessageOf;
        this.timestamp = timestamp;
        this.status = status;
    }
}
