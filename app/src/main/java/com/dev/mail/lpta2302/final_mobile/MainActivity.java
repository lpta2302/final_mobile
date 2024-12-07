package com.dev.mail.lpta2302.final_mobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.mail.lpta2302.final_mobile.mail.Mail;
import com.dev.mail.lpta2302.final_mobile.mail.MailService;
//Tìm trong build.gradle, trong scope android,
//thêm những dòng này vào để có thể xài
//        (làm như branch này là chạy)
//packaging {
//    // Exclude the conflicting files
//    resources.excludes.add("META-INF/NOTICE.md")
//    resources.excludes.add( "META-INF/NOTICE")
//    resources.excludes.add("META-INF/LICENSE.md")
//    resources.excludes.add("META-INF/LICENSE")

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

        new Thread(() -> {
                try {
                MailService.sendEmail(
                        Mail.builder()
                                .subject("Test")
                                .to("anluong.31221020084@st.ueh.edu.vn")
                                .content("Hello World").build()
                );
        runOnUiThread(() -> Toast.makeText(this, "Email sent successfully!", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
        runOnUiThread(() -> Toast.makeText(this, "Failed to send email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
                }).start();
    }
}