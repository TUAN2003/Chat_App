package com.example.chat_app.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.databinding.ItemContainerUserBinding;
import com.example.chat_app.fragments.ListFriendFragment;
import com.example.chat_app.listeners.UserListenser;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private final List<User> users;
    private final UserListenser userListenser;

    public UsersAdapter(List<User> users, UserListenser userListenser) {
        this.users = users;
        this.userListenser = userListenser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false);
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        @SuppressLint("NotifyDataSetChanged")
        void setUserData(User user) {
            binding.layout.setOnLongClickListener(v -> {
                final Context context=((ListFriendFragment)userListenser).getContext();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.unfriend_dialog);

                Window window = dialog.getWindow();
                if (window == null) {
                    return false;
                }
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);

                Button buttonNegative = dialog.findViewById(R.id.buttonNegativeUnfriend);
                Button buttonPositive = dialog.findViewById(R.id.buttonPositiveCancel1);

                buttonNegative.setOnClickListener(v1 -> {
                    unFriend(user);
                    dialog.cancel();
                    users.remove(user);
                    notifyDataSetChanged();
                    Toast.makeText(context,"Xóa thành công",Toast.LENGTH_SHORT).show();
                });
                buttonPositive.setOnClickListener(v1 -> dialog.cancel());
                dialog.show();
                return true;
            });
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.layout.setOnClickListener(v -> userListenser.onUserClicked(user));
            binding.buttonCall.setOnClickListener(v -> userListenser.onUserClickedCall(user));
            binding.buttonVideoCall.setOnClickListener(v -> userListenser.onUserClickedVideoCall(user));
        }
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void unFriend(User user){
        FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                .document(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID))
                .update(Constants.KEY_LIST_FRIEND, FieldValue.arrayRemove(user.id));
        FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                .document(user.id)
                .update(Constants.KEY_LIST_FRIEND
                        , FieldValue.arrayRemove(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID)));
    }
}
