package com.example.chat_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.chat_app.R;
import com.example.chat_app.adapters.ViewPager2Adapter;
import com.example.chat_app.utilities.Constants;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ContainerFragmentActivity extends BaseActivity {
    private NavigationView navigationView;
    private NavigationBarView bottomNavigationView;
    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private int indexFragment = 0;

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
        //header
        DrawerLayout rootView = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, rootView, toolbar, R.string.open_drawer, R.string.close_drawer);
        rootView.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //body
        viewPager = findViewById(R.id.fragmentContainer);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager.setAdapter(viewPager2Adapter);
        //footer
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView = findViewById(R.id.nav_view);
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.headerCopyright);
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());

        // Đổi màu của từng chữ trong văn bản
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.rgb(14, 128, 241));
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.rgb(255, 50, 0));
        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.rgb(0, 210, 0));

        builder.setSpan(redSpan, 10, 11, 0); // Đổi màu ký tự 0 (H) thành màu đỏ
        builder.setSpan(greenSpan, 11, 12, 0); // Đổi màu ký tự 6 (W) thành màu xanh lá
        builder.setSpan(blueSpan, 12, 13, 0); // Đổi màu ký tự 8 (o) thành màu xanh dương

        textView.setText(builder);
    }

    private void setListeners() {
        this.bottomNavigationView.setOnItemSelectedListener(navListener);
        this.navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.itemMessageGroup).setChecked(true);
                        changeFragment(1);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.itemFriends).setChecked(true);
                        changeFragment(2);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.itemChatBot).setChecked(true);
                        changeFragment(3);
                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.itemMessage).setChecked(true);
                        changeFragment(0);
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
                changeFragment(1);
            } else if (idSelectedItem == idItemFriends) {
                viewPager.setCurrentItem(2, false);
                changeFragment(2);
            } else if (idSelectedItem == idItemChatBot) {
                viewPager.setCurrentItem(3, false);
                changeFragment(3);
            } else {
                viewPager.setCurrentItem(0, false);
                changeFragment(0);
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        switch (indexFragment) {
            case 1:
                getMenuInflater().inflate(R.menu.menu_group_chat, menu);
                break;
            case 2:
                getMenuInflater().inflate(R.menu.menu_list_friend, menu);
                break;
            case 3:
                getMenuInflater().inflate(R.menu.menu_chat_bot, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.menu_home_fragment, menu);
        }
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

    private void changeFragment(int position) {
        indexFragment = position;
        toolbar.getMenu().clear();
        switch (position) {
            case 1:
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_group_chat);
                toolbar.inflateMenu(R.menu.menu_group_chat);
                break;
            case 2:
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.label_friends);
                toolbar.inflateMenu(R.menu.menu_list_friend);
                break;
            case 3:
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.chat_ai);
                toolbar.inflateMenu(R.menu.menu_chat_bot);
                break;
            default:
                ContainerFragmentActivity.this.toolbar.setTitle(R.string.text_title_home);
                toolbar.inflateMenu(R.menu.menu_home_fragment);

        }
    }

    private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        if (item.getItemId() == R.id.itemLogout)
            signOut();
        return true;
    };

    private void signOut() {
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection(Constants.KEY_COLLECTION_USERS).document(
                        SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        updates.put(Constants.KEY_STATUS,null);
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    SignInActivity.preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                });
    }
}