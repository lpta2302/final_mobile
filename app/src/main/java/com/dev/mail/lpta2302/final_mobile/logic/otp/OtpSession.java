package com.dev.mail.lpta2302.final_mobile.logic.otp;

import android.os.Handler;
import android.os.Looper;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpSession {
    @Setter @Getter
    private String otp;
    @Setter @Getter
    private OtpStatus status = OtpStatus.INACTIVE;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final int[] elapsedTime = {0};
    private final int interval = 1;

    private boolean flag;

    public OtpSession(String otp) {
        this.otp = otp;
    }

    public void startSession(int timeoutInSeconds, OtpTimerCallback callback) {
        status = OtpStatus.PENDING;

        startTimer(timeoutInSeconds, new OtpTimerCallback() {
            @Override
            public void onTick(int currentSecond) {
                callback.onTick(currentSecond);
            }

            @Override
            public void onFinish() {
                status = OtpStatus.EXPIRED;
                callback.onFinish();
            }

            @Override
            public void onBreak(int currentSecond) {
                status = OtpStatus.INACTIVE;
                callback.onBreak(currentSecond);
            }
        });
    }

    public boolean verify(String otp) {
        if (Objects.equals(this.otp, otp) && status == OtpStatus.PENDING) {
            status = OtpStatus.VERIFIED;
            return true;
        }
        else return false;
    }

    private void startTimer(int timeoutInSeconds, OtpTimerCallback callback) {
        if (!flag) {
            flag = true;
            elapsedTime[0] = timeoutInSeconds;

            scheduler.scheduleWithFixedDelay(() -> {
                // Huỷ timer.
                if (!flag) {
                    scheduler.shutdown();
                    new Handler(Looper.getMainLooper()).post(() -> callback.onBreak(elapsedTime[0]));
                }
                elapsedTime[0]--;

                // Mỗi lần tick sẽ gọi hàm.
                new Handler(Looper.getMainLooper()).post(() -> callback.onTick(elapsedTime[0]));

                // Timer hoàn thành.
                if (elapsedTime[0] <= 0) {
                    scheduler.shutdown();
                    new Handler(Looper.getMainLooper()).post(callback::onFinish);
                }
            }, 1, interval, TimeUnit.SECONDS);
        }
    }

    public void breakSession() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
            flag = false;
        }
    }
}
