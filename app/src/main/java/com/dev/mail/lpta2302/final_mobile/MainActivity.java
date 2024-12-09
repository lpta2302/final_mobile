package com.dev.mail.lpta2302.final_mobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
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
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
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

        checkConnection();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void checkConnection() {
        String TAG = "INTERNET";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connectivityManager.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.e(TAG, "The default network is now: " + network);
            }

            @Override
            public void onLost(Network network) {
                Log.e(TAG, "The application no longer has a default network. The last default network was " + network);
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                Log.e(TAG, "The default network changed capabilities: " + networkCapabilities);
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                Log.e(TAG, "The default network changed link properties: " + linkProperties);
            }});
    }
}