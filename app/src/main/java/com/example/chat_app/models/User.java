package com.example.chat_app.models;

import java.io.Serializable;
import java.util.Comparator;

public class User implements Serializable {
    public String id, name, image, email, token,numberPhone,password;
    public String[] listIdFriend;
    public String[] listIdGroup;

    public User(){}
    public User(String id, String name, String image, String email, String token, String[] listIdFriend) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
        this.listIdFriend = listIdFriend;
    }
    public User(String id, String name, String image, String email, String token,String numberPhone, String[] listIdFriend) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
        this.listIdFriend = listIdFriend;
        this.numberPhone=numberPhone;
    }

}
