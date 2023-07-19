package com.example.chat_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.chat_app.databinding.ActivityUserInformationBinding;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserInformationActivity extends AppCompatActivity {
    private User user;
    private ActivityUserInformationBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = SignInActivity.preferenceManager;
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(Constants.KEY_USER);
        loadProfile();
        setListeners();
    }

    private void loadProfile() {
        byte[] bytes = Base64.decode(user.image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        this.binding.imageProfile.setImageBitmap(bitmap);
        this.binding.inputName.setText(user.name);
        this.binding.inputEmail.setText(user.email);
        if (user.id.equals(preferenceManager.getString(Constants.KEY_USER_ID)))
            this.binding.sendButton.setVisibility(View.INVISIBLE);
    }

    private void setListeners() {
        this.binding.backButton.setOnClickListener(v -> onBackPressed());
        this.binding.sendButton.setOnClickListener(v -> sendInvitation());
    }

    private void sendInvitation() {
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        objectHashMap.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
        objectHashMap.put(Constants.KEY_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
        objectHashMap.put(Constants.KEY__RECEIVER_ID, user.id);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_INVITATIONS)
                .add(objectHashMap)
                .addOnSuccessListener(documentReference -> {
                            documentReference.update(Constants.KEY_INVITATION_ID, documentReference.getId());
                            Toast.makeText(getApplicationContext(), "Gửi lời mời kết bạn thành công", Toast.LENGTH_SHORT).show();
                        }
                )
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Gửi lời mời không thành công", Toast.LENGTH_SHORT).show());
    }
}