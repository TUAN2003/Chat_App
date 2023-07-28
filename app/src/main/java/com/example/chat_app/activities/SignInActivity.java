package com.example.chat_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat_app.R;
import com.example.chat_app.databinding.ActivitySignInBinding;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.FunctionGlobal;
import com.example.chat_app.utilities.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private boolean isPasswordVisible = false;
    public static PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = PreferenceManager.newInstance(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            startActivity(new Intent(getApplicationContext(), ContainerFragmentActivity.class));
            finish();
            return;
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        User fromSignUp;
        if ((fromSignUp = (User) getIntent().getSerializableExtra(Constants.KEY_USER)) != null) {
            this.binding.inputEmail.setText(fromSignUp.email);
            this.binding.inputPassword.setText(fromSignUp.password);
        }
        setListeners();
    }

    private void setListeners() {
        binding.buttonSignIn.setOnClickListener(v -> {
            if (isValidSignUpDetails())
                signIn();
        });
        binding.buttonSignUp.setOnClickListener(v ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
        binding.textForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        //Display Password
        final EditText editTextPassword = binding.inputPassword;
        final Drawable icVisible = getResources().getDrawable(R.drawable.ic_display_password);
        final Drawable icInvisible = getResources().getDrawable(R.drawable.ic_hide_password);

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        int indexSelection = editTextPassword.getSelectionEnd();
                        isPasswordVisible = !isPasswordVisible;
                        if (isPasswordVisible) {
                            editTextPassword.setTransformationMethod(null);
                            editTextPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, icInvisible, null);
                        } else {
                            editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                            editTextPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, icVisible, null);
                        }
                        editTextPassword.setSelection(indexSelection);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, this.binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, this.binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        Intent intent = new Intent(getApplicationContext(), ContainerFragmentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading(false);
                        showToast("Sai tài khoản hoặc mật khẩu");
                    }
                });
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setText("");
            binding.animationView.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignIn.setText(R.string.sign_in);
            binding.animationView.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidSignUpDetails() {
        String inputEmail = this.binding.inputEmail.getText().toString();
        String password = this.binding.inputPassword.getText().toString();
        if (inputEmail.isEmpty()) {
            showToast("Nhập địa chỉ email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            showToast("Không đúng định dạng của email");
            return false;
        } else if (password.isEmpty()) {
            showToast("Nhập mật khẩu");
            return false;
        }
        return true;
    }
}