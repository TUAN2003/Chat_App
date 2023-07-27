package com.example.chat_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chat_app.R;
import com.example.chat_app.adapters.ViewPager2Adapter;
import com.google.android.material.navigation.NavigationBarView;

public class ContainerFragmentActivity extends AppCompatActivity {
    private NavigationBarView bottomNavigationView;
    private ViewPager2 viewPager;
    private Toolbar toolbar;

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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.fragmentContainer);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager.setAdapter(viewPager2Adapter);
        DrawerLayout rootView = findViewById(R.id.drawer_layout);
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
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.itemMessageGroup).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_group_chat);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.itemFriends).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.itemChatBot).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.itemMessage).setChecked(true);
                        ContainerFragmentActivity.this.toolbar.setTitle(R.string.text_title_home);
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
            } else if (idSelectedItem == idItemFriends) {
                viewPager.setCurrentItem(2, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
            } else if (idSelectedItem == idItemChatBot) {
                viewPager.setCurrentItem(3, false);
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
            } else {
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