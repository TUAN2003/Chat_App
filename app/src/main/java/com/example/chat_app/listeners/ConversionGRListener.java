package com.example.chat_app.listeners;

import com.example.chat_app.models.GroupChat;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface ConversionGRListener {
    void onClick(GroupChat groupChats);
    void onClickDeleteBottomSheet(GroupChat groupChat, BottomSheetDialog dialog);
}
