package com.dev.mail.lpta2302.final_mobile.navigation.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.navigation.friends.Friend;
import com.dev.mail.lpta2302.final_mobile.navigation.friends.FriendsAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private List<Notification> notificationList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Thiết lập RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dữ liệu mẫu danh sách bạn bè
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("Bạn A", R.drawable.avatar1, "xxxx"));
        notificationList.add(new Notification("Bạn B", R.drawable.avatar2, "xxxx"));

        // Gắn Adapter
        notificationsAdapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(notificationsAdapter);

        return root;
    }
}