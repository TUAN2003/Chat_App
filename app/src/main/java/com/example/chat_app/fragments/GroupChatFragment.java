package com.example.chat_app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_app.activities.ChatGroupActivity;
import com.example.chat_app.activities.ContainerFragmentActivity;
import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.adapters.RecentConversationGroupAdapter;
import com.example.chat_app.databinding.FragmentGroupChatBinding;
import com.example.chat_app.listeners.ConversionGRListener;
import com.example.chat_app.models.GroupChat;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GroupChatFragment extends Fragment implements ConversionGRListener {
    private FragmentGroupChatBinding binding;
    private ContainerFragmentActivity parentActivity;
    private List<GroupChat> conversations;
    private RecentConversationGroupAdapter conversationGroupAdapter;
    private FirebaseFirestore database;

    public GroupChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (ContainerFragmentActivity) requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGroupChatBinding.inflate(inflater, container, false);
        init();
        listenConversations();
        return binding.getRoot();
    }

    private void init() {
        conversations = new ArrayList<>();
        conversationGroupAdapter =
                new RecentConversationGroupAdapter(conversations, this);
        binding.conversationRecyclerView.setAdapter(conversationGroupAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void listenConversations() {
        String userId = SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID);
        database.collection(Constants.KEY_COLLECTION_GROUPS)
                .whereArrayContains(Constants.KEY_ID_MEMBERS, userId)
                .addSnapshotListener(eventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null)
            return;
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                String nameGroup = documentChange.getDocument().getString(Constants.KEY_NAME_GROUP);
                String enCodeImage = documentChange.getDocument().getString(Constants.KEY_ENCODE_IMAGE);
                String idGroup = documentChange.getDocument().getId();
                String lastMessage = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                Date date = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                List<String> idMembers = (List<String>) documentChange.getDocument().get(Constants.KEY_ID_MEMBERS);
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    GroupChat groupChat = new GroupChat();
                    groupChat.setNameGroup(nameGroup);
                    groupChat.setEnCodeImage(enCodeImage);
                    groupChat.setDate(date);
                    groupChat.setIdGroup(idGroup);
                    groupChat.setLastMessage(lastMessage);
                    groupChat.setIdMember(idMembers);
                    conversations.add(groupChat);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        if (conversations.get(i).getIdGroup().equals(idGroup)) {
                            conversations.get(i).setNameGroup(nameGroup);
                            conversations.get(i).setDate(date);
                            conversations.get(i).setLastMessage(lastMessage);
                            conversations.get(i).setEnCodeImage(enCodeImage);
                            conversations.get(i).setIdMember(idMembers);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            conversationGroupAdapter.notifyDataSetChanged();
            binding.conversationRecyclerView.scrollToPosition(0);
            binding.conversationRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onClick(GroupChat groupChats) {
        Intent intent = new Intent(parentActivity.getApplicationContext(), ChatGroupActivity.class);
        intent.putExtra(Constants.KEY_COLLECTION_GROUPS, groupChats);
        startActivity(intent);
    }
}