package com.dev.mail.lpta2302.final_mobile.logic.user;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.dev.mail.lpta2302.final_mobile.logic.cache.CacheDatabaseClient;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.dev.mail.lpta2302.final_mobile.logic.util.RemoveVietnameseDiacritics;

import java.util.ArrayList;
import java.util.List;

public class UserCacheService {
    private UserCacheService(Context context) {
        this.context = context;
    }
    public static UserCacheService getInstance(Context context) {
        return new UserCacheService(context);
    }

    private final Context context;

    public void insert(User user, QueryCallback<Void> callback) {
        new Thread(() -> {
            try {
                CacheDatabaseClient.getInstance(context)
                        .userDao()
                        .insert(user);

                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(null));
            }
            catch (Exception e) {
                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }

    public void insertUsers(List<User> users, @Nullable QueryCallback<Void> callback) {
        new Thread(() -> {
            try {
                CacheDatabaseClient.getInstance(context)
                        .userDao()
                        .insertUsers(users);

                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(null));
            }
            catch (Exception e) {
                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }

    public void findById(String id, QueryCallback<User> callback) {
        new Thread(() -> {
            try {
                User user = CacheDatabaseClient.getInstance(context)
                        .userDao()
                        .findById(id);

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(user));
            }
            catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }

    public void findByEmail(String email, QueryCallback<User> callback) {
        new Thread(() -> {
            try {
                User user = CacheDatabaseClient.getInstance(context)
                        .userDao()
                        .findByEmail(email);

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(user));
            }
            catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }

    public void searchByFullName(String keyword, QueryCallback<List<User>> callback) {
        new Thread(() -> {
            try {
                List<User> users = CacheDatabaseClient.getInstance(context)
                        .userDao()
                        .findAll();

                List<User> result = new ArrayList<>();
                for (User user : users) {
                    if (RemoveVietnameseDiacritics.removeDiacritics(user.getFullName()).trim()
                            .contains(RemoveVietnameseDiacritics.removeDiacritics(keyword).trim()))
                        result.add(user);
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(result));
            }
            catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }

    public void clear(@Nullable QueryCallback<Void> callback) {
        new Thread(() -> {
            try {
                CacheDatabaseClient.getInstance(context)
                        .userDao()
                        .deleteAll();

                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(null));
            }
            catch (Exception e) {
                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }
}
