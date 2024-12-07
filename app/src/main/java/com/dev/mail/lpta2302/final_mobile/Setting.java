package com.dev.mail.lpta2302.final_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Nhận các ChipButton
        Chip chipEditProfile = findViewById(R.id.chipEditProfile);
        Chip chipChangePassword = findViewById(R.id.chipChangePassword);
        Chip chipLogout = findViewById(R.id.chipLogout);

        // Sự kiện nhấn chip "Chỉnh sửa hồ sơ"
        chipEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, ManageYourProfile.class);
                startActivity(intent);
            }
        });

        // Sự kiện nhấn chip "Đổi mật khẩu"
        chipChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Sự kiện nhấn chip "Đăng xuất"
        chipLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị AlertDialog
                new AlertDialog.Builder(Setting.this)
                        .setMessage("Bạn có chắc chắn muốn thoát?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Chuyển hướng về trang Login
                                Intent intent = new Intent(Setting.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Đóng activity hiện tại
                            }
                        })
                        .setNegativeButton("Không", null) // Đóng dialog nếu chọn "Không"
                        .show();
            }
        });
    }
}
