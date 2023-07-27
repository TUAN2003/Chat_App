package com.example.chat_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chat_app.fragments.ChatBotFragment;
import com.example.chat_app.fragments.GroupChatFragment;
import com.example.chat_app.fragments.HomeFragment;
import com.example.chat_app.fragments.ListFriendFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    private static final Fragment[] fragments = new Fragment[]{
            new HomeFragment()
            , new GroupChatFragment()
            , new ListFriendFragment()
            , new ChatBotFragment()};

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
