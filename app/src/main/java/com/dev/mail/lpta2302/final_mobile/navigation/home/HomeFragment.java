package com.dev.mail.lpta2302.final_mobile.navigation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;

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

        // Tạo danh sách các bài viết giả
        postList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            postList.add(new Post(i + " likes"));  // Hình ảnh avatar1 đã được set cố định trong Adapter
        }

        // Gán adapter vào RecyclerView
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        return rootView;
    }
}
