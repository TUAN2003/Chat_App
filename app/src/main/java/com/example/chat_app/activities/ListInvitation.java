package com.example.chat_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.chat_app.adapters.InvitationAdapter;
import com.example.chat_app.databinding.ActivityListInvitationBinding;
import com.example.chat_app.models.Invitation;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListInvitation extends AppCompatActivity {
    private ActivityListInvitationBinding binding;
    private List<Invitation> invitations;
    private InvitationAdapter invitationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListInvitationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListener();
        listenerInvitation();
    }

    private void init() {
        invitations = new ArrayList<>();
        invitationAdapter = new InvitationAdapter(invitations);
        binding.usersRecyclerView.setAdapter(invitationAdapter);
    }

    private void listenerInvitation() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_INVITATIONS)
                .whereEqualTo(Constants.KEY__RECEIVER_ID, SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private void setListener() {
        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null)
            return;
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                String id = documentChange.getDocument().getId();
                String image = documentChange.getDocument().getString(Constants.KEY_IMAGE);
                String name = documentChange.getDocument().getString(Constants.KEY_NAME);
                String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                String receiverId = documentChange.getDocument().getString(Constants.KEY__RECEIVER_ID);
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Invitation invitation = new Invitation(id, senderId, receiverId, image, name);
                    invitations.add(invitation);
                }
            }
            invitationAdapter.notifyDataSetChanged();
            binding.usersRecyclerView.scrollToPosition(0);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
            this.binding.footer.setText(invitations.size() + " lời mời");
        }
    };
}