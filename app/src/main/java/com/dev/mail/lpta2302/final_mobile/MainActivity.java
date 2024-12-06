package com.dev.mail.lpta2302.final_mobile;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.user.UserRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        UserRepository.instance.create(new User("test","tyest","an","thien"), new ExpectationAndException() {
            @Override
            public void call(Exception exception, Object expectation) {
                if(expectation != null){
                    Log.d("create user",expectation.toString());
                    UserRepository.instance.findById((String) expectation, new ExpectationAndException() {
                        @Override
                        public void call(Exception exception, Object expectation) {
                            AuthUser.getInstance().setUser((User) expectation);
                            PostService.getInstance().createPost(
                                    Post.builder()
                                            .caption("test")
                                            .build(),
                                    new PostService.CreateCallback() {
                                        @Override
                                        public void onSuccess(Post post) {
                                            Log.d("Create Post","Yes");
                                            PostService.getInstance().readPosts(
                                                    new PostService.ReadCallback() {
                                                        @Override
                                                        public void onSuccess(List<Post> posts) {
                                                            Log.d("Read post","Yes");

                                                            for (Post post :
                                                                    posts) {
                                                                Log.d("POST",post.toString());
                                                                if(post.getAuthor() != null)Log.d("CHECK USER","Yes");
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Exception e) {

                                                        }
                                                    }
                                            );
                                        }

                                        @Override
                                        public void onFailure(Exception e) {

                                        }
                                    }
                            );
                        }
                    });
                }
            }
        });
    }

}