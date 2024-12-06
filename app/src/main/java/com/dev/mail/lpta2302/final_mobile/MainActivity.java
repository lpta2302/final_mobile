package com.dev.mail.lpta2302.final_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.user.UserRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button createUserBtn, updateUserBtn, createPostBtn, showUserBtn, showPostBtn;
    private User user;
    private Post post;
    private final View.OnClickListener createUser = (v)->{
        UserRepository.instance
            .create(new User("lpta2302@gmail.com","0909","thien","an"),(e,res)->{
                Log.d("CREATE USER", res.toString());
                UserRepository.instance.findById(res.toString(), (ex, resUser)->{
                    user = (User) resUser;
                    AuthUser.getInstance().setUser(user);
                });
            });
    };
    private final View.OnClickListener createPost= (v)->{
        PostService.getInstance().createPost(
                Post.builder()
                        .caption("Hello World")
                        .authorId(user.getId())
                        .build(),
                new PostService.CreateCallback() {
                    @Override
                    public void onSuccess(Post post) {
                        Log.d("create post", post.toString());
                        Log.d("author", post.getAuthorId().toString());
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }
        );
    };
    private final View.OnClickListener updateUser= (v)->{
        user.setEmail("updated");
        UserRepository.instance.update(
                user,
                (e,res)->{
                    Log.d("updateUser", "Updated");
                }
        );
    };
    private final View.OnClickListener showUser= (v)->{
        UserRepository.instance.findById(user.getId(), (ex, resUser)->{
            Log.d("ShowUser", ((User) resUser).toString());
        });
    };
    private final View.OnClickListener showPost= (v)->{
        PostService.getInstance().searchPostByUser(user.getId(), new PostService.SearchCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                for (Post post :
                        posts) {
                    Log.d("post " + post.getId(), post.toString());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.test_layout);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        createUserBtn = findViewById(R.id.createUser);
        createPostBtn = findViewById(R.id.createPost);
        updateUserBtn = findViewById(R.id.updateUser);
        showUserBtn = findViewById(R.id.showUser);
        showPostBtn = findViewById(R.id.showPost);

        createUserBtn.setOnClickListener(createUser);
        createPostBtn.setOnClickListener(createPost);
        updateUserBtn.setOnClickListener(updateUser);
        showUserBtn.setOnClickListener(showUser);
        showPostBtn.setOnClickListener(showPost);

    }


}