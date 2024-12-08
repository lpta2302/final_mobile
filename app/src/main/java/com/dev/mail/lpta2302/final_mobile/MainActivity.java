package com.dev.mail.lpta2302.final_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dev.mail.lpta2302.final_mobile.activities.addnews.AddnewsFragment;
import com.dev.mail.lpta2302.final_mobile.activities.auth.LoginActivity;
import com.dev.mail.lpta2302.final_mobile.activities.friends.FriendsFragment;
import com.dev.mail.lpta2302.final_mobile.activities.home.HomeFragment;
import com.dev.mail.lpta2302.final_mobile.activities.notifications.NotificationsFragment;
import com.dev.mail.lpta2302.final_mobile.activities.options.OptionsFragment;
import com.dev.mail.lpta2302.final_mobile.activities.search.SearchFragment;
import com.dev.mail.lpta2302.final_mobile.databinding.ActivityMainBinding;
import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Toolbar toolbar;
    private  final Map<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>(){};
    private final NavigationBarView.OnItemSelectedListener selectListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.home)
                replaceFragment(new HomeFragment());
            if(id == R.id.liked)
                replaceFragment(new HomeFragment());
            if(id == R.id.friends)
                replaceFragment(new FriendsFragment());
            if(id == R.id.create)
                replaceFragment(new AddnewsFragment());
            if(id == R.id.menu)
                replaceFragment(new OptionsFragment());
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.notification){
            replaceFragment(new NotificationsFragment());
        } else if (id == R.id.search){
            replaceFragment(new SearchFragment());
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AuthUser.getInstance().isAuthenticated()){
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setBackground(null);
            binding.bottomNavigationView.setOnItemSelectedListener(selectListener);
        } else{
            Intent intent = new Intent(this, LoginActivity.class); //add login acctivity to change
            startActivity(intent);
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}