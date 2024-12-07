package com.dev.mail.lpta2302.final_mobile.util;

public interface QueryCallback<T> {
    void onSuccess(T expectation);
    void onFailure(Exception exception);
}
