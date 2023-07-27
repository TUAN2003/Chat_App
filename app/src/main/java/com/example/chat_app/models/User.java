package com.example.chat_app.models;

import java.io.Serializable;

public class User implements Serializable {
    public String id, name, image, email, token,numberPhone,password;
    public String[] listIdFriend;

    public User(){}
}
