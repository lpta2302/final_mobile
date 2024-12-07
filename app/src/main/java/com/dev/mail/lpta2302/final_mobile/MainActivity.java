package com.dev.mail.lpta2302.final_mobile;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

import java.util.List;

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

        PostService.getInstance().searchPostsByCaption("ted", new QueryCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                posts.forEach(post->{
                    Log.d("post", post.toString());
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}