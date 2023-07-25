package com.example.chat_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chat_app.R;
import com.example.chat_app.adapters.ViewPager2Adapter;
import com.example.chat_app.fragments.ChatBotFragment;
import com.example.chat_app.fragments.GroupChatFragment;
import com.example.chat_app.fragments.HomeFragment;
import com.example.chat_app.fragments.ListFriendFragment;
import com.google.android.material.navigation.NavigationBarView;

public class ContainerFragmentActivity extends BaseActivity {
    private NavigationBarView bottomNavigationView;
    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private int cursorFragment=0;
    public static Fragment[] fragments = new Fragment[]{
            new HomeFragment()
            , new GroupChatFragment()
            , new ListFriendFragment()
            , new ChatBotFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_fragment);
        init();
    }

    private void init() {
        bindingView();
        setListeners();
    }

    private void bindingView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout rootView = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.fragmentContainer);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager.setAdapter(viewPager2Adapter);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, rootView, toolbar, R.string.open_drawer, R.string.close_drawer);
        rootView.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setListeners() {
        this.bottomNavigationView.setOnItemSelectedListener(navListener);
        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        cursorFragment=0;
                        bottomNavigationView.getMenu().findItem(R.id.itemMessage).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.text_title_home);
                        break;
                    case 1:
                        cursorFragment=1;
                        bottomNavigationView.getMenu().findItem(R.id.itemMessageGroup).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_group_chat);
                        break;
                    case 2:
                        cursorFragment=2;
                        bottomNavigationView.getMenu().findItem(R.id.itemFriends).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
                        break;
                    case 3:
                        cursorFragment=3;
                        bottomNavigationView.getMenu().findItem(R.id.itemChatBot).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
                        break;
                }
            }
        });
    }

    private final NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final int idSelectedItem = item.getItemId();
            final int idItemFriends = R.id.itemFriends;
            final int idItemGroupChat = R.id.itemMessageGroup;
            final int idItemChatBot = R.id.itemChatBot;
            if (idSelectedItem == idItemGroupChat) {
                cursorFragment=1;
                viewPager.setCurrentItem(1, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_group_chat);
            } else if (idSelectedItem == idItemFriends) {
                cursorFragment=2;
                viewPager.setCurrentItem(2, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
            } else if (idSelectedItem == idItemChatBot) {
                cursorFragment=3;
                viewPager.setCurrentItem(3, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
            } else {
                cursorFragment=0;
                viewPager.setCurrentItem(0, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.text_title_home);
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int result = item.getItemId();
        if (result == R.id.itemSearch) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (result == R.id.itemAddFriend) {
            startActivity(new Intent(this, AddFriendActivity.class));
            return true;
        }
        return false;
    }
}