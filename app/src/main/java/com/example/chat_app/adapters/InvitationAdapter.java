package com.example.chat_app.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.databinding.ItemInvitationBinding;
import com.example.chat_app.models.Invitation;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {
    private final List<Invitation> invitations;
    private int mCount;

    public InvitationAdapter(List<Invitation> invitations) {
        this.invitations = invitations;
        mCount=invitations.size();
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvitationViewHolder(ItemInvitationBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        holder.setData(invitations.get(position), position);
    }

    @Override
    public int getItemCount() {
        mCount = invitations.size();
        return mCount;
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder {
        ItemInvitationBinding itemInvitationBinding;

        public InvitationViewHolder(ItemInvitationBinding itemInvitationBinding) {
            super(itemInvitationBinding.getRoot());
            this.itemInvitationBinding = itemInvitationBinding;
        }

        public void setData(Invitation invitation, int position) {
            this.itemInvitationBinding.imageProfile.setImageBitmap(getBitmapImage(invitation.image));
            this.itemInvitationBinding.textName.setText(invitation.name);
            this.itemInvitationBinding.textDeny.setOnClickListener(v -> onClickDeny(invitation));
            this.itemInvitationBinding.textAccept.setOnClickListener(v -> onClickAccept(invitation));
            if (position == mCount - 1)
                itemInvitationBinding.lineBottom.setVisibility(View.INVISIBLE);
            else
                itemInvitationBinding.lineBottom.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getBitmapImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onClickDeny(Invitation invitation) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_INVITATIONS).document(invitation.id).delete();
        this.invitations.remove(invitation);
        notifyDataSetChanged();
    }

    private void onClickAccept(Invitation invitation) {
        onClickDeny(invitation);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID))
                .update(Constants.KEY_LIST_FRIEND, FieldValue.arrayUnion(invitation.senderId));
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(invitation.senderId)
                .update(Constants.KEY_LIST_FRIEND, FieldValue.arrayUnion(invitation.receiverId));
    }
}
