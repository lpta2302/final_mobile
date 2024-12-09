package com.dev.mail.lpta2302.final_mobile.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.home.PostAdapter;
import com.dev.mail.lpta2302.final_mobile.logic.post.Post;
import com.dev.mail.lpta2302.final_mobile.logic.post.PostService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}