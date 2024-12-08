package com.dev.mail.lpta2302.final_mobile.signin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.dev.mail.lpta2302.final_mobile.CacheDatabaseClient;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

public class SignInSessionCacheService {
    private SignInSessionCacheService(Context context) {
        this.context =context;
    }

    private final Context context;

    public static SignInSessionCacheService getInstance(Context context) {
        return new SignInSessionCacheService(context);
    }

    public void create(User user, @Nullable QueryCallback<Void> callback) {
        new Thread(() -> {
            try {
                SignInSession session = new SignInSession("0", user.getId());
                CacheDatabaseClient.getInstance(context)
                        .signInSessionDao()
                        .insert(session);

                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(null));
            }
            catch (Exception e) {
                if (callback != null)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }

    public void get(QueryCallback<User> callback) {
        new Thread(() -> {
            try {
                SignInSession sessions = CacheDatabaseClient.getInstance(context)
                        .signInSessionDao()
                        .findById("0");

                if (sessions != null) {
                    User user = CacheDatabaseClient.getInstance(context)
                            .userDao()
                            .findById(sessions.getUserId());

                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(user));
                }
                else new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(new Exception("UserNotFound")));
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
                        .signInSessionDao()
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
