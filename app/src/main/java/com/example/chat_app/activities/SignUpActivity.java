package com.example.chat_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;

import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chat_app.R;
import com.example.chat_app.databinding.ActivitySignUpBinding;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String encodedImageDefault;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        encodedImageDefault =
                encodeImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar_default_2));
        setListeners();
    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails())
                signUp();
        });
        binding.layoutImage.setOnClickListener(v -> {
            final int idPickImage = R.id.itemPickImage;
            final int idDropImage = R.id.itemDropImage;
            PopupMenu popupMenu = new PopupMenu(SignUpActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_clicked_image, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == idPickImage) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    pickImage.launch(intent);
                    return true;
                } else if (item.getItemId() == idDropImage) {
                    this.binding
                            .imageProfile
                            .setImageBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.avatar_default_2));
                    this.encodedImage = null;
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        String name = binding.inputName.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_NAME, name)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().getDocuments().size() == 0) {
                        HashMap<String, Object> user = new HashMap<>();
                        user.put(Constants.KEY_NAME, name);
                        user.put(Constants.KEY_EMAIL, email);
                        user.put(Constants.KEY_PASSWORD, password);
                        if (encodedImage != null)
                            user.put(Constants.KEY_IMAGE, encodedImage);
                        else
                            user.put(Constants.KEY_IMAGE, encodedImageDefault);
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .add(user)
                                .addOnSuccessListener(documentReference -> {
                                    loading(false);
                                    documentReference.update(Constants.KEY_USER_ID, documentReference.getId());
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    User user1 = new User();
                                    user1.email = Objects.requireNonNull(user.get(Constants.KEY_EMAIL)).toString();
                                    user1.password = Objects.requireNonNull(user.get(Constants.KEY_PASSWORD)).toString();
                                    intent.putExtra(Constants.KEY_USER, user1);
                                    startActivity(intent);
                                }).addOnFailureListener(e -> {
                                    loading(false);
                                    showToast(e.getMessage());
                                });
                    } else {
                        loading(false);
                        showToast("tên người dùng đã tồn tại");
                    }
                });
    }

    private boolean isValidSignUpDetails() {
        String inputName = this.binding.inputName.getText().toString();
        String inputEmail = this.binding.inputEmail.getText().toString();
        String password = this.binding.inputPassword.getText().toString();
        String confirmPassword = binding.inputConfirmPassword.getText().toString();
        if (inputName.isEmpty()) {
            showToast("Bạn chưa nhập tên");
            return false;
        } else if (inputEmail.isEmpty()) {
            showToast("Bạn chưa nhập email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            showToast("Cần nhập đúng định dạng email");
            return false;
        } else if (password.isEmpty()) {
            showToast("Bạn cần nhập mật khẩu");
            return false;
        } else if (!confirmPassword.equals(password)) {
            showToast("Mật khẩu chưa chính xác");
            return false;
        }
        return true;
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            this.binding.imageProfile.setImageBitmap(bitmap);
                            this.encodedImage = this.encodeImage(bitmap);
                        } catch (FileNotFoundException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
    );
}