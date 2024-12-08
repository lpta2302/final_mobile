package com.dev.mail.lpta2302.final_mobile.activities.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;

public class OtpActivity extends AppCompatActivity {

    private long timeLeftInMillis = 60000; // 60 giây
    private TextView timerTextView;

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
        startCountdownTimer();

        // Nút xác minh OTP
        Button verifyOtpButton = findViewById(R.id.verifyOtpButton);
        verifyOtpButton.setOnClickListener(v -> {
            // Lấy OTP từ EditText
            EditText otpInput = findViewById(R.id.otpInput);
            String otp = otpInput.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            } else if (otp.length() != 6) {
                Toast.makeText(this, "Định dạng mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                // Giả lập logic xác minh OTP
                if (otp.equals("123456")) { // Giả sử mã OTP hợp lệ là "123456"
                    Toast.makeText(this, "Xác minh OTP thành công", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình chính
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Mã OTP không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút quay lại
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // Kết thúc Activity và quay lại màn hình trước đó
        });
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