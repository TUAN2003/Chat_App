package com.example.chat_app.models;

public class Invitation {
    public String senderId,receiverId,image,name,id;

    public Invitation(String id,String senderId, String receiverId, String image, String name) {
        this.id=id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.image = image;
        this.name = name;
    }
    public Invitation(){}
}
