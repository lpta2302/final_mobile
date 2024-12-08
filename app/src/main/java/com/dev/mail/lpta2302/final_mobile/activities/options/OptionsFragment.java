package com.dev.mail.lpta2302.final_mobile.activities.options;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.ChangePassword;
import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.home.PostAdapter;
import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_options, container, false);

        // Initialize RecyclerView and data
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PostService.getInstance().readPosts(new QueryCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> expectation) {
                Log.d("found",String.valueOf(expectation.size()));
                postList.clear();
                postList.addAll(expectation);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                postList = new ArrayList<>();
                postAdapter = new PostAdapter(postList);
                recyclerView.setAdapter(postAdapter);
            }
        });
        view.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUser.getInstance().setUser(null);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
