package com.example.chat_app.models;

import java.util.Date;

public class Conversation {
    public String conversationId;
    public String receiverId,receiverName,receiverImage;
    public String lastMessage,newMessageOf;
    public Date timestamp;
    public boolean status = false;
}
