package com.example.chat_app.listeners;

import com.example.chat_app.models.Conversation;
import com.example.chat_app.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface ConversionListener {
    void onConversionClicked(User user,String conversationId,String newMessageOf);
    void onClickDeleteBottomSheet(Conversation conversation, BottomSheetDialog dialog);
}
