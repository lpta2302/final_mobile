package com.dev.mail.lpta2302.final_mobile;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.mail.lpta2302.final_mobile.mail.SendMailCallback;
import com.dev.mail.lpta2302.final_mobile.otp.OtpService;
import com.dev.mail.lpta2302.final_mobile.otp.OtpTimerCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        OtpService.getInstance().startOtpSession("tranhaiduong2004t@gmail.com", 10, new OtpTimerCallback() {
//            @Override
//            public void onTick(int currentSecond) {
//                Log.d("OTP Service", "Current time: " + currentSecond);
//            }
//
//            @Override
//            public void onFinish() {
//                Log.d("OTP Service", "OTP is expired!");
//            }
//
//            @Override
//            public void onBreak(int currentSecond) {
//
//            }
//        }, new SendMailCallback() {
//            @Override
//            public void onSuccess() {
//                Log.d("OTP Mail Sender", "Send mail successfully!");
//            }
//
//            @Override
//            public void onFailure(Exception exception) {
//                Log.e("OTP Mail Sender", "Failed to send the email");
//            }
//        });

    }
}