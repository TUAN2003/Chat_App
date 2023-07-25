package com.example.chat_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;

import com.example.chat_app.adapters.ChatGroupAdapter;
import com.example.chat_app.databinding.ActivityChatGroupBinding;
import com.example.chat_app.models.ChatMessage;
import com.example.chat_app.models.GroupChat;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatGroupActivity extends AppCompatActivity {

    private ActivityChatGroupBinding binding;
    private List<ChatMessage> chatMessages;
    private HashMap<String,Bitmap> usersImage;
    private ChatGroupAdapter chatGroupAdapter;
    private FirebaseFirestore database;
    private GroupChat groupChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        loadReceiverDetails();
        init();
    }

    private void setListener() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
        binding.inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    binding.layoutSend.setVisibility(View.GONE);
                else
                    binding.layoutSend.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_GROUP_ID, groupChat.getIdGroup());
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        updateConversion(binding.inputMessage.getText().toString().trim()
                ,SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID));
        binding.inputMessage.setText("");
    }

    private void loadReceiverDetails() {
        groupChat = (GroupChat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_GROUPS);
        binding.textName.setText(groupChat.getNameGroup());
    }

    private void init() {
        binding.progressBar.setVisibility(View.VISIBLE);
        database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereIn(Constants.KEY_USER_ID,groupChat.getIdMember())
                .get()
                .addOnCompleteListener(task -> {
                    usersImage = new HashMap<>();
                    for(DocumentSnapshot query:task.getResult().getDocuments())
                        usersImage.put(query.getId(),getBitmapFromEncodedString(query.getString(Constants.KEY_IMAGE)));
                    chatMessages = new ArrayList<>();
                    chatGroupAdapter = new ChatGroupAdapter(chatMessages, usersImage, SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID));
                    binding.chatRecyclerView.setAdapter(chatGroupAdapter);
                    listenerMessage();
                    binding.progressBar.setVisibility(View.INVISIBLE);
                });
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    private void listenerMessage() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_GROUP_ID, groupChat.getIdGroup())
                .addSnapshotListener(eventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null)
            return;
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.groupId = documentChange.getDocument().getString(Constants.KEY_GROUP_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessage.dateTime = getReadableDateTime(chatMessage.dateObject);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (o1, o2) -> o1.dateObject.compareTo(o2.dateObject));
            if (count == 0) {
                chatGroupAdapter.notifyDataSetChanged();
            } else {
                chatGroupAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("HH:mm dd MMMM", Locale.getDefault()).format(date);
    }

    private void updateConversion(String message,String watcheds) {
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_GROUPS)
                        .document(groupChat.getIdGroup());
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message
                ,Constants.KEY_WATCHEDS,watcheds
                , Constants.KEY_TIMESTAMP, new Date());
    }
}