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
        DrawerLayout rootView = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.fragmentContainer);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager.setAdapter(viewPager2Adapter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                        bottomNavigationView.getMenu().findItem(R.id.itemMessage).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.text_title_home);
                        displayMenuFragmentChange(0);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.itemMessageGroup).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_group_chat);
                        displayMenuFragmentChange(1);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.itemFriends).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
                        displayMenuFragmentChange(2);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.itemChatBot).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
                        displayMenuFragmentChange(3);
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
                viewPager.setCurrentItem(1, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_group_chat);
                displayMenuFragmentChange(1);
            } else if (idSelectedItem == idItemFriends) {
                viewPager.setCurrentItem(2, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
                displayMenuFragmentChange(2);
            } else if (idSelectedItem == idItemChatBot) {
                viewPager.setCurrentItem(3, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
                displayMenuFragmentChange(3);
            } else {
                viewPager.setCurrentItem(0, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.text_title_home);
                displayMenuFragmentChange(0);
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_user, menu);
        toolbar.getMenu().findItem(R.id.itemAdditional).setVisible(false);
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

    private void displayMenuFragmentChange(int index) {
        toolbar.getMenu().clear();
        if (index == 2) {
            toolbar.inflateMenu(R.menu.menu_fragment_list_friend);
        } else if (index == 0) {
            toolbar.inflateMenu(R.menu.menu_option_user);
            toolbar.getMenu().findItem(R.id.itemAdditional).setVisible(false);
        } else {
            toolbar.inflateMenu(R.menu.menu_option_user);
        }
    }
}