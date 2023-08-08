package com.example.chat_app.models;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Group implements Serializable {
    public String id;
    public String image;
    public String name;
    public List<String> idMembers;
    public String lastMessage;
    public List<String> seenMessages;
    public Date dateTime;
    public boolean status = false;

    public Group(String id, String image, String name, List<String> idMembers, String lastMessage, List<String> seenMessages, Date dateTime, boolean status) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.idMembers = idMembers;
        this.lastMessage = lastMessage;
        this.seenMessages = seenMessages;
        this.dateTime = dateTime;
        this.status = status;
    }
    public Group(){}
}
