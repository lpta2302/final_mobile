package com.dev.mail.lpta2302.final_mobile.logic.otp;

public interface OtpTimerCallback {
    void onTick(int currentSecond);
    void onFinish();
    void onBreak(int currentSecond);
}