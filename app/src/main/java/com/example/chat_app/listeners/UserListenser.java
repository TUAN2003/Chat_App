package com.example.chat_app.listeners;

import com.example.chat_app.models.User;

public interface UserListenser {
    void onUserClicked(User user);
    void onUserClickedCall(User user);
    void onUserClickedVideoCall(User user);
}
