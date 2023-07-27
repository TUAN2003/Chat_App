package com.example.chat_app.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.databinding.ItemContainerReceivedMessageBinding;
import com.example.chat_app.databinding.ItemContainerSentMessageBinding;
import com.example.chat_app.models.Message;

import java.util.HashMap;
import java.util.List;

public class ChatGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<Message> chatMessages;
    private final HashMap<String,Bitmap> users;
    private final String senderId;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;


    public ChatGroupAdapter(List<Message> chatMessages, HashMap<String,Bitmap> users, String senderId) {
        this.chatMessages = chatMessages;
        this.users=users;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT)
            return new ChatGroupAdapter.SentMessageViewHolder(ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            ));
        return new ChatGroupAdapter.ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((ChatGroupAdapter.SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            String key=chatMessages.get(position).senderId;
            ((ChatGroupAdapter.ReceivedMessageViewHolder) holder).setData(chatMessages.get(position)
                    ,users.get(key));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(this.senderId))
            return VIEW_TYPE_SENT;
        return VIEW_TYPE_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(Message chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding item) {
            super(item.getRoot());
            binding = item;
        }

        void setData(Message chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if (receiverProfileImage != null) {
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
        }
    }
}
