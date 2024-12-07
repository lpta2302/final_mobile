package com.dev.mail.lpta2302.final_mobile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dev.mail.lpta2302.final_mobile.databinding.ActivityMainBinding;
import com.dev.mail.lpta2302.final_mobile.fragments.CreateFragment;
import com.dev.mail.lpta2302.final_mobile.fragments.FriendsFragment;
import com.dev.mail.lpta2302.final_mobile.fragments.HomeFragment;
import com.dev.mail.lpta2302.final_mobile.fragments.LikedFragment;
import com.dev.mail.lpta2302.final_mobile.fragments.MenuFragment;
import com.dev.mail.lpta2302.final_mobile.fragments.NotificationFragment;
import com.dev.mail.lpta2302.final_mobile.fragments.SearchFragment;
import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
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
                replaceFragment(new LikedFragment());
            if(id == R.id.friends)
                replaceFragment(new FriendsFragment());
            if(id == R.id.create)
                replaceFragment(new CreateFragment());
            if(id == R.id.menu)
                replaceFragment(new MenuFragment());
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
            replaceFragment(new NotificationFragment());
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
//            Intent intent = new Intent(this, LoginActivity.class); //add login acctivity to change
//            startActivity(intent);
            finish();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}