package com.example.chat_app.listeners;

import com.example.chat_app.models.Conversation;
import com.example.chat_app.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface ConversationListener {
    void onConversationClicked(User user,Conversation conversation);
    void onClickDeleteBottomSheet(Conversation conversation, BottomSheetDialog dialog);
}
