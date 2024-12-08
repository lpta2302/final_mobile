package com.dev.mail.lpta2302.final_mobile.mail;

public interface SendMailCallback {
    void onSuccess();
    void onFailure(Exception exception);
}
