package com.dev.mail.lpta2302.final_mobile.logic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

import com.dev.mail.lpta2302.final_mobile.MainActivity;

public class NetworkUtil {

    public static void checkInternetConnection(Context context) {
//        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(Network network) {
//                Log.e(TAG, "The default network is now: " + network);
//            }
//
//            @Override
//            public void onLost(Network network) {
//                Log.e(TAG, "The application no longer has a default network. The last default network was " + network);
//            }
//
//            @Override
//            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
//                Log.e(TAG, "The default network changed capabilities: " + networkCapabilities);
//            }
//
//            @Override
//            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
//                Log.e(TAG, "The default network changed link properties: " + linkProperties);
//            }
//        });
    }
}