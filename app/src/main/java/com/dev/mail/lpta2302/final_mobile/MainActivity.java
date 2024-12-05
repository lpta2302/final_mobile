package com.dev.mail.lpta2302.final_mobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.mail.lpta2302.final_mobile.Post.Post;
import com.dev.mail.lpta2302.final_mobile.Post.PostService;
import com.dev.mail.lpta2302.final_mobile.util.ImageUploadService;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    ImageUploadService.getInstance().uploadImage(uri, new ImageUploadService.OnImageUploadListener() {
                        @Override
                        public void onSuccess(String imageUrl) {
                            createPost(imageUrl);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                        }
                    });
                }
            });
    private void createPost(String uri){
        Post post = Post.builder()
                .imageUrl(uri)
                .build();
        PostService.getInstance().createPost(post, new PostService.CreateCallback() {
            @Override
            public void onSuccess(Post post) {
                Toast.makeText(MainActivity.this,"yey", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.createBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");


            }
        });
    }

}