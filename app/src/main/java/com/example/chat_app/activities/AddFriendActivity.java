package com.example.chat_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chat_app.databinding.ActivityAddFriendBinding;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AddFriendActivity extends AppCompatActivity {
    private ActivityAddFriendBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.textViewInvitation.setOnClickListener(v -> startActivity(new Intent(AddFriendActivity.this,ListInvitation.class)));
        binding.buttonRequest.setOnClickListener(v -> searchFriend(binding.inputEmail.getText().toString().trim()));
    }

    public void searchFriend(String email){
        FirebaseFirestore fireStore=FirebaseFirestore.getInstance();
        fireStore.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,email)
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot queryResult=task.getResult();
                    if (queryResult.getDocuments().size() == 0) {
                        Toast.makeText(getApplicationContext(),"Không tìm thấy", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                    User user=new User();
                    user.id=documentSnapshot.getId();
                    user.name=documentSnapshot.getString(Constants.KEY_NAME);
                    user.image=documentSnapshot.getString(Constants.KEY_IMAGE);
                    user.email=email;
                    Intent intent=new Intent(AddFriendActivity.this,UserInformationActivity.class);
                    intent.putExtra(Constants.KEY_USER,user);
                    startActivity(intent);
                });
    }
}