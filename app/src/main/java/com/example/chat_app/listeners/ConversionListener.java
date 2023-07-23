package com.example.chat_app.listeners;

import com.example.chat_app.models.ChatMessage;
import com.example.chat_app.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface ConversionListener {
    void onConversionClicked(User user);
    void onClickDeleteBottomSheet(ChatMessage chatMessage, BottomSheetDialog dialog);
}
