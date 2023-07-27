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
import com.example.chat_app.fragments.GroupChatFragment;
import com.example.chat_app.listeners.ConversionGRListener;
import com.example.chat_app.models.Group;
import com.example.chat_app.utilities.Constants;
import com.example.chat_app.utilities.FunctionGlobal;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class RecentConversationGroupAdapter extends RecyclerView.Adapter<RecentConversationGroupAdapter.ConversionGroupViewHolder> {
    private final List<Group> groups;
    private final ConversionGRListener conversionGRListener;
    private int mCount;

    public RecentConversationGroupAdapter(List<Group> groups, ConversionGRListener conversionGRListener) {
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

        void setData(Group groupChat, int position) {
            binding.imageProfile.setImageBitmap(getConversionImage(groupChat.getEnCodeImage()));
            binding.textName.setText(groupChat.getNameGroup());
            binding.textRecentMessage.setText(groupChat.getLastMessage());
            binding.textTimeStamp.setText(FunctionGlobal.dateTimeFormat(groupChat.getDate()));
            binding.getRoot().setOnClickListener(v ->
                    conversionGRListener.onClick(groupChat,groupChat.getIdGroup(),groupChat.getWatcheds()));
            binding.getRoot().setOnLongClickListener(v -> {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(((GroupChatFragment)conversionGRListener).requireContext());
                bottomSheetDialog.setContentView(R.layout.bottomsheet_option_conversation_group);
                View view=bottomSheetDialog.findViewById(R.id.deleteConversationGroup);
                assert view != null;
                view.setOnClickListener(v1 -> conversionGRListener.onClickDeleteBottomSheet(groupChat,bottomSheetDialog));
                bottomSheetDialog.show();
                return true;
            });
            if(groupChat.getWatcheds().contains(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID)))
            {
                binding.textName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                binding.textRecentMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                binding.textRecentMessage.setTextColor(((GroupChatFragment)conversionGRListener).getResources().getColor(R.color.secondary_text));
                binding.newMessage.setVisibility(View.GONE);
            }
            else{
                binding.textName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.textRecentMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.textRecentMessage.setTextColor(Color.BLACK);
                binding.newMessage.setVisibility(View.VISIBLE);
            }
            if(position == mCount-1)
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
