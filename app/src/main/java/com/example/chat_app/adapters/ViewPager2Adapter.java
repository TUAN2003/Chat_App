package com.example.chat_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chat_app.activities.ContainerFragmentActivity;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ContainerFragmentActivity.fragments[position];
    }

    @Override
    public int getItemCount() {
        return ContainerFragmentActivity.fragments.length;
    }
}
