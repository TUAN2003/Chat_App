package com.example.chat_app.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.databinding.ItemContainerRecentConversionBinding;
import com.example.chat_app.listeners.ConversionGRListener;
import com.example.chat_app.models.GroupChat;
import com.example.chat_app.utilities.FunctionGlobal;

import java.util.List;

public class RecentConversationGroupAdapter extends RecyclerView.Adapter<RecentConversationGroupAdapter.ConversionGroupViewHolder> {
    private final List<GroupChat> groups;
    private final ConversionGRListener conversionGRListener;
    private int mCount;

    public RecentConversationGroupAdapter(List<GroupChat> groups, ConversionGRListener conversionGRListener) {
        this.groups = groups;
        this.conversionGRListener = conversionGRListener;
        this.mCount=groups.size();
    }

    @NonNull
    @Override
    public RecentConversationGroupAdapter.ConversionGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentConversationGroupAdapter.ConversionGroupViewHolder(
                ItemContainerRecentConversionBinding.inflate(LayoutInflater.from(parent.getContext())
                        , parent
                        , false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationGroupAdapter.ConversionGroupViewHolder holder, int position) {
        holder.setData(groups.get(position),position);
    }

    @Override
    public int getItemCount() {
        mCount=groups.size();
        return mCount;
    }

    class ConversionGroupViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversionBinding binding;

        ConversionGroupViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversationBinding) {
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(GroupChat groupChat,int position) {
            binding.imageProfile.setImageBitmap(getConversionImage(groupChat.getEnCodeImage()));
            binding.textName.setText(groupChat.getNameGroup());
            binding.textRecentMessage.setText(groupChat.getLastMessage());
            binding.textTimeStamp.setText(FunctionGlobal.dateTimeFormat(groupChat.getDate()));
            if(position == mCount-1)
                binding.lineBottom.setVisibility(View.INVISIBLE);
            else
                binding.lineBottom.setVisibility(View.VISIBLE);
            binding.getRoot().setOnClickListener(v -> conversionGRListener.onClick(groupChat));
        }
    }

    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
