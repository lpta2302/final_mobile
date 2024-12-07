package com.dev.mail.lpta2302.final_mobile.navigation.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;
    private List<Friend> friendsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        // Thiết lập RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dữ liệu mẫu danh sách bạn bè
        friendsList = new ArrayList<>();
        friendsList.add(new Friend("Bạn A", R.drawable.avatar1));
        friendsList.add(new Friend("Bạn B", R.drawable.avatar2));

        // Gắn Adapter
        friendsAdapter = new FriendsAdapter(friendsList);
        recyclerView.setAdapter(friendsAdapter);

        return root;
    }
}
