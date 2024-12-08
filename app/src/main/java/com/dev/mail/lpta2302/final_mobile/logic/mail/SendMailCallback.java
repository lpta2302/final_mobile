package com.dev.mail.lpta2302.final_mobile.logic.mail;

public interface SendMailCallback {
    void onSuccess();
    void onFailure(Exception exception);
}
