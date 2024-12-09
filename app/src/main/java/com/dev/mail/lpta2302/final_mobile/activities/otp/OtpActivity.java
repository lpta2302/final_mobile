package com.dev.mail.lpta2302.final_mobile.activities.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.auth.SignupActivity;
import com.dev.mail.lpta2302.final_mobile.logic.mail.SendMailCallback;
import com.dev.mail.lpta2302.final_mobile.logic.otp.OtpService;
import com.dev.mail.lpta2302.final_mobile.logic.otp.OtpTimerCallback;

public class OtpActivity extends AppCompatActivity {

    private long timeLeftInMillis = 60000; // 60 giây
    private TextView timerTextView;
    public static final String OTP_RESULT_TAG = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Lấy email từ Intent
        String userEmail = getIntent().getStringExtra("email");

        // Hiển thị email trong TextView
        TextView emailTextView = findViewById(R.id.emailTextView);
        if (userEmail != null) {
            emailTextView.setText("OTP đã được gửi tới: " + userEmail);
        }

        // Tham chiếu đến TextView đếm ngược
        timerTextView = findViewById(R.id.timerTextView);

        // Bắt đầu đếm ngược
        // startCountdownTimer();

        // Nút xác minh OTP
        Button verifyOtpButton = findViewById(R.id.verifyOtpButton);
        verifyOtpButton.setOnClickListener(v -> {
            // Lấy OTP từ EditText
            EditText otpInput = findViewById(R.id.otpInput);
            String otp = otpInput.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            } else {
                if (OtpService.getInstance().verifyOtp(otp)) {
                    sendResult(true);
                } else {
                    Toast.makeText(this, "Mã OTP không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra(SignupActivity.EMAIL_TAG);

        OtpService.getInstance().startOtpSession(email, 120, new OtpTimerCallback() {
            @Override
            public void onTick(int currentSecond) {
                int minutes = currentSecond / 60;
                int remainingSeconds = currentSecond % 60;
                String timeText = "Thời gian còn lại: " + String.format("%02d:%02d", minutes, remainingSeconds);
                timerTextView.setText(timeText);
            }

            @Override
            public void onFinish() {
                sendResult(true);
            }

            @Override
            public void onBreak(int currentSecond) {}
        }, new SendMailCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Mã OTP đã gửi đến hộp thư của bạn.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                OtpService.getInstance().breakOtpSession();
                sendResult(false);
            }
        });

        // Nút quay lại
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            OtpService.getInstance().breakOtpSession();
            sendResult(false);
        });
    }

    private void sendResult(boolean result) {
        Intent resultIntend = new Intent();
        resultIntend.putExtra(OTP_RESULT_TAG, result);
        setResult(RESULT_OK, resultIntend);
        finish();
    }

    private void startCountdownTimer() {
        new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (timeLeftInMillis / 1000);
                timerTextView.setText("Vui lòng chờ " + seconds + " giây để gửi lại.");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Bạn có thể yêu cầu gửi lại mã OTP.");
                // Thêm logic kích hoạt nút gửi lại nếu cần
            }
        }.start();
    }
}