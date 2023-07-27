package com.example.chat_app.listeners;

import com.example.chat_app.models.Group;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public interface ConversionGRListener {
    void onClick(Group groupChats, String documentId, List<String> watcheds);
    void onClickDeleteBottomSheet(Group groupChat, BottomSheetDialog dialog);
}
