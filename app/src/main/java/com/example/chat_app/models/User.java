package com.example.chat_app.models;

import java.io.Serializable;

public class User implements Serializable {
    public String id, name, image, email, token,numberPhone,password;
    public String[] listIdFriend;
    public User(){}

    public User(String id, String name, String image, String email, String token, String numberPhone, String password, String[] listIdFriend) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
        this.numberPhone = numberPhone;
        this.password = password;
        this.listIdFriend = listIdFriend;
    }
}
