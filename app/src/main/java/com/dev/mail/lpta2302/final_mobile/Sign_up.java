package com.dev.mail.lpta2302.final_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class Sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Nút Sign Up (tạm thời không có xử lý)
        Button signUpConfirmButton = findViewById(R.id.signUpConfirmButton);
        signUpConfirmButton.setOnClickListener(v -> {
            // Xử lý đăng ký tại đây
        });

        // Nút quay lại màn hình Login
        Button loginLinkButton = findViewById(R.id.loginLinkButton);
        loginLinkButton.setOnClickListener(v -> {
            Intent intent = new Intent(Sign_up.this, MainActivity.class);
            startActivity(intent);
        });
    }
}