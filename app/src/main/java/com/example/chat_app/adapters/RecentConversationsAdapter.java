package com.example.chat_app.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.databinding.ItemContainerRecentConversionBinding;
import com.example.chat_app.fragments.HomeFragment;
import com.example.chat_app.listeners.ConversationListener;
import com.example.chat_app.models.Conversation;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.FunctionGlobal;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {
    private final List<Conversation> conversations;
    private final ConversationListener conversationListener;
    private int mCount;

    public RecentConversationsAdapter(List<Conversation> conversations, ConversationListener conversationListener) {
        this.conversations = conversations;
        this.conversationListener = conversationListener;
        this.mCount = conversations.size();
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversionBinding.inflate(LayoutInflater.from(parent.getContext())
                        , parent
                        , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        mCount = conversations.size();
        return mCount;
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversionBinding binding;

        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversationBinding) {
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(int position) {
            Conversation conversation = conversations.get(position);
            binding.imageProfile.setImageBitmap(getConversionImage(conversation.receiverImage));
            binding.textName.setText(conversation.receiverName);
            binding.textRecentMessage.setText(conversation.lastMessage);
            binding.textTimeStamp.setText(FunctionGlobal.dateTimeFormat(conversation.timestamp));
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = conversation.receiverId;
                user.name = conversation.receiverName;
                user.image = conversation.receiverImage;
                user.email = conversation.receiverEmail;
                user.token = conversation.receiverToken;
                user.numberPhone = conversation.receiverNumberPhone;
                conversationListener.onConversationClicked(user, conversation);
            });
            binding.getRoot().setOnLongClickListener(v -> {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(((HomeFragment) conversationListener).requireContext());
                bottomSheetDialog.setContentView(R.layout.bottomsheet_option_conversation);
                View view = bottomSheetDialog.findViewById(R.id.deleteConversation);
                assert view != null;
                view.setOnClickListener(v1 -> conversationListener.onClickDeleteBottomSheet(conversation, bottomSheetDialog));
                bottomSheetDialog.show();
                return true;
            });
            if (conversation.newMessageOf.equals(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID))) {
                binding.textName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.textRecentMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.textRecentMessage.setTextColor(Color.BLACK);
                binding.newMessage.setVisibility(View.VISIBLE);
            } else {
                binding.textName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                binding.textRecentMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                binding.textRecentMessage.setTextColor(((HomeFragment) conversationListener).getResources().getColor(R.color.secondary_text));
                binding.newMessage.setVisibility(View.GONE);
            }
            if (position == mCount - 1)
                binding.lineBottom.setVisibility(View.INVISIBLE);
            else
                binding.lineBottom.setVisibility(View.VISIBLE);
            updateStatus(conversation.status);
        }
        public void updateStatus(boolean status){
            if (status) {
                binding.status.setVisibility(View.VISIBLE);
            } else {
                binding.status.setVisibility(View.INVISIBLE);
            }
        }
    }

    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
