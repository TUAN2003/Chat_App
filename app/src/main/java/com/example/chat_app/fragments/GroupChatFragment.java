package com.example.chat_app.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_app.R;
import com.example.chat_app.activities.ChatGroupActivity;
import com.example.chat_app.activities.ContainerFragmentActivity;
import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.adapters.RecentConversationGroupAdapter;
import com.example.chat_app.databinding.FragmentGroupChatBinding;
import com.example.chat_app.listeners.ConversationGRListener;
import com.example.chat_app.models.Group;
import com.example.chat_app.utilities.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GroupChatFragment extends Fragment implements ConversationGRListener {
    private FragmentGroupChatBinding binding;
    private ContainerFragmentActivity parentActivity;
    private List<Group> conversations;
    private RecentConversationGroupAdapter conversationGroupAdapter;
    private FirebaseFirestore database;

    public GroupChatFragment() {
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
        binding.conversationRecyclerView.setItemAnimator(new RecyclerView.ItemAnimator() {
            @Override
            public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(@NonNull RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
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
                String name = documentChange.getDocument().getString(Constants.KEY_NAME_GROUP);
                String image = documentChange.getDocument().getString(Constants.KEY_ENCODE_IMAGE);
                String id = documentChange.getDocument().getId();
                List<String> seenMessages = (List<String>) documentChange.getDocument().get(Constants.KEY_SEEN_MESSAGES);
                String lastMessage = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                Date dateTime = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                List<String> idMembers = (List<String>) documentChange.getDocument().get(Constants.KEY_ID_MEMBERS);
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Group groupChat = new Group(
                            id,image,name,idMembers,lastMessage,seenMessages,dateTime,false
                    );
                    conversations.add(groupChat);
                    if(idMembers != null){
                        List<String> list = new ArrayList<>(idMembers);
                        list.remove(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID));
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .whereIn(Constants.KEY_USER_ID,list)
                                .addSnapshotListener((value1, error1) -> {
                                    if (error1 != null)
                                        return;
                                    if(value1 != null){
                                        Boolean b = null;
                                        for(DocumentChange doc:value1.getDocumentChanges()){
                                            b = doc.getDocument().getBoolean(Constants.KEY_STATUS);
                                            if(b != null && b)
                                                break;
                                        }
                                        groupChat.status = (b != null && b);
                                        conversationGroupAdapter.notifyItemChanged(conversations.indexOf(groupChat));
                                    }
                                });
                    }
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        if (conversations.get(i).id.equals(id)) {
                            conversations.get(i).name = name;
                            conversations.get(i).dateTime = dateTime;
                            conversations.get(i).lastMessage = lastMessage;
                            conversations.get(i).image = image;
                            conversations.get(i).idMembers = idMembers;
                            conversations.get(i).seenMessages = seenMessages;
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (o1, o2) -> o2.dateTime.compareTo(o1.dateTime));
            conversationGroupAdapter.notifyDataSetChanged();
            binding.conversationRecyclerView.scrollToPosition(0);
            binding.conversationRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onClick(Group groupChats) {
        if (!groupChats.seenMessages.contains(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID))) {
            database.collection(Constants.KEY_COLLECTION_GROUPS)
                    .document(groupChats.id)
                    .update(Constants.KEY_SEEN_MESSAGES, FieldValue.arrayUnion(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID)));
        }
        Intent intent = new Intent(parentActivity.getApplicationContext(), ChatGroupActivity.class);
        intent.putExtra(Constants.KEY_COLLECTION_GROUPS, groupChats);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClickDeleteBottomSheet(Group groupChat, BottomSheetDialog dialog) {
        dialog.cancel();
        final AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Rời nhóm")
                .setMessage("Bạn có muốn rời nhóm \"" + groupChat.name + "\" không?")
                .setNegativeButton("Rời nhóm", (dialog1, which) -> {
                    GroupChatFragment.this.database.collection(Constants.KEY_COLLECTION_GROUPS)
                            .document(groupChat.id)
                            .update(Constants.KEY_ID_MEMBERS,
                                    FieldValue.arrayRemove(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID)))
                            .addOnSuccessListener(unused -> {
                                conversations.remove(groupChat);
                                conversationGroupAdapter.notifyDataSetChanged();
                            });
                    dialog1.cancel();
                }).setPositiveButton("Hủy", (dialog1, which) -> dialog1.cancel());
        AlertDialog alertDialog = builder.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.main_color_blue));
    }
}