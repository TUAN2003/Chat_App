package com.example.chat_app.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_app.activities.ChatActivity;
import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.adapters.UsersAdapter;
import com.example.chat_app.databinding.FragmentListFrirendBinding;
import com.example.chat_app.listeners.UserListenser;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListFriendFragment extends Fragment implements UserListenser {
    private FragmentListFrirendBinding binding;
    private UsersAdapter usersAdapter;
    private List<User> users;

    public ListFriendFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListFrirendBinding.inflate(inflater, container, false);
        init();
        getUsers();
        return binding.getRoot();
    }

    private void init(){
        users = new ArrayList<>();
        usersAdapter  = new UsersAdapter(users,this);
        binding.usersRecyclerView.setAdapter(usersAdapter);
    }
    @Override
    public void onUserClicked(User user) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Context context = activity.getApplicationContext();
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, user);
            startActivity(intent);
        }
    }

    @Override
    public void onUserClickedCall(User user) {
        Uri uri = Uri.parse("tel:" + user.numberPhone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    @Override
    public void onUserClickedVideoCall(User user) {

    }

    private void loading(boolean isLoading) {
        if (isLoading)
            binding.progressBar.setVisibility(View.VISIBLE);
        else
            binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void showIfListEmptyOrNull() {
        binding.textListEmpty.setText(String.format("%s", "Danh sách trống"));
        binding.textListEmpty.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String myId = SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID);
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(myId);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                    loading(false);
                    final List<String> listFriendId = (List<String>) documentSnapshot.get(Constants.KEY_LIST_FRIEND);
                    if (listFriendId == null || listFriendId.size() == 0){
                        showIfListEmptyOrNull();
                        return;
                    }
                    database.collection(Constants.KEY_COLLECTION_USERS)
                            .whereIn(Constants.KEY_USER_ID, listFriendId)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    for (QueryDocumentSnapshot item : task.getResult()) {
                                        User user = new User(
                                                item.getId(),
                                                item.getString(Constants.KEY_NAME),
                                                item.getString(Constants.KEY_IMAGE),
                                                item.getString(Constants.KEY_EMAIL),
                                                item.getString(Constants.KEY_FCM_TOKEN),
                                                item.getString(Constants.KEY_NUMBER_PHONE),
                                                null,
                                                null
                                        );
                                        users.add(user);
                                    }
                                    usersAdapter.notifyDataSetChanged();
                                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
                                }
                                else
                                    showIfListEmptyOrNull();
                            });
                }
        );
    }
}