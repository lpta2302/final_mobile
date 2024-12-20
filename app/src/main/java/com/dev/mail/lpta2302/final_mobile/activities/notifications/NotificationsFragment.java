package com.dev.mail.lpta2302.final_mobile.activities.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.logic.notification.Notification;
import com.dev.mail.lpta2302.final_mobile.logic.notification.NotificationService;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;

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
        notificationsAdapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(notificationsAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NotificationService.getInstance().findAll(AuthUser.instance.getUser(), new QueryCallback<List<com.dev.mail.lpta2302.final_mobile.logic.notification.Notification>>() {
            @Override
            public void onSuccess(List<com.dev.mail.lpta2302.final_mobile.logic.notification.Notification> expectation) {
                notificationList.clear();
                notificationList.addAll(expectation);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }
}