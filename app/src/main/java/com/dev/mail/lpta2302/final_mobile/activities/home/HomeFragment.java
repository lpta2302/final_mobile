package com.dev.mail.lpta2302.final_mobile.activities.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo RecyclerView và danh sách bài viết
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
    }


}
