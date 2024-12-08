package com.dev.mail.lpta2302.final_mobile.activities.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.home.PostAdapter;
import com.dev.mail.lpta2302.final_mobile.logic.post.Post;
import com.dev.mail.lpta2302.final_mobile.logic.post.PostService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private EditText searchEt;
    private ImageView searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize RecyclerView and data
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        // Initialize search components
        searchEt = rootView.findViewById(R.id.searchEt);
        searchBtn = rootView.findViewById(R.id.searchBtn);

        // Handle search action
        searchBtn.setOnClickListener(v -> handleSearch());

        return rootView;
    }

    private void handleSearch() {
        String query = searchEt.getText().toString().trim();
        postList.clear();

        if (TextUtils.isEmpty(query)) {
            // Clear results if input is empty
            postList.clear();
            postAdapter.notifyDataSetChanged();
            return;
        }

        if (query.startsWith("#")) {
            // Extract tags if input contains hashtags
            String[] tags = query.substring(1, query.length() - 1).split("#");
            searchByTags(Arrays.asList(tags));
        } else {
            // Search by caption if input is plain text
            searchByCaption(query);
        }
    }

    private void searchByTags(List<String> tags) {
        PostService.getInstance().searchPostsByTags(tags, new QueryCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> result) {
                postList.addAll(result);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle error, e.g., show a message to the user
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    private void searchByCaption(String caption) {
        PostService.getInstance().searchPostsByCaption(caption, new QueryCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> result) {
                postList.addAll(result);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle error, e.g., show a message to the user
                postAdapter.notifyDataSetChanged();
            }
        });
    }
}
