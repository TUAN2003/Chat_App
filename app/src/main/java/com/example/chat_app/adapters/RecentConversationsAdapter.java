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
import com.example.chat_app.listeners.ConversionListener;
import com.example.chat_app.models.ChatMessage;
import com.example.chat_app.models.User;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.FunctionGlobal;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {
    private final List<ChatMessage> chatMessages;
    private final ConversionListener conversionListener;
    private int mCount;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversionListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
        this.mCount = chatMessages.size();
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
        mCount = chatMessages.size();
        return mCount;
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversionBinding binding;

        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversationBinding) {
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(int position) {
            ChatMessage chatMessage=chatMessages.get(position);
            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.conversionImage));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.textTimeStamp.setText(FunctionGlobal.dateTimeFormat(chatMessage.dateObject));
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.receiverId;
                user.name = chatMessage.conversionName;
                user.image = chatMessage.conversionImage;
                conversionListener.onConversionClicked(user,chatMessage.conversionId,chatMessage.newMessageOf);
            });
            binding.getRoot().setOnLongClickListener(v -> {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(((HomeFragment) conversionListener).requireContext());
                bottomSheetDialog.setContentView(R.layout.bottomsheet_option_conversation);
                View view = bottomSheetDialog.findViewById(R.id.deleteConversation);
                assert view != null;
                view.setOnClickListener(v1 -> conversionListener.onClickDeleteBottomSheet(chatMessage, bottomSheetDialog));
                bottomSheetDialog.show();
                return true;
            });
            if(chatMessage.newMessageOf.equals(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID)))
            {
                binding.textName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.textRecentMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.textRecentMessage.setTextColor(Color.BLACK);
                binding.newMessage.setVisibility(View.VISIBLE);
            }
            else{
                binding.textName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                binding.textRecentMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                binding.textRecentMessage.setTextColor(((HomeFragment)conversionListener).getResources().getColor(R.color.secondary_text));
                binding.newMessage.setVisibility(View.GONE);
            }
            if (position == mCount - 1)
                binding.lineBottom.setVisibility(View.INVISIBLE);
            else
                binding.lineBottom.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
