package com.dev.mail.lpta2302.final_mobile.friend;

import androidx.annotation.Nullable;

import com.dev.mail.lpta2302.final_mobile.Notification;
import com.dev.mail.lpta2302.final_mobile.NotificationService;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @Setter
    private String id;
    @Setter
    private User user1;
    @Setter
    private User user2;
    @Setter
    private LocalDateTime createdAt;
    private FriendStatus status;

    public Friendship(User user1, User user2, LocalDateTime createdAt, FriendStatus status){
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = createdAt;
        this.status = status;
    }

    public void sendRequest(@Nullable Notification notification, QueryCallback<String> callback) {
        this.status = FriendStatus.PENDING;
        if (notification != null) NotificationService.getInstance().create(notification, callback);
    }

    public void acceptRequest(@Nullable Notification notification, QueryCallback<String> callback) {
        this.status = FriendStatus.ACCEPTED;
        createdAt = LocalDateTime.now();

        if (notification != null) NotificationService.getInstance().create(notification, callback);
    }

    public void declineRequest(@Nullable Notification notification, QueryCallback<Void> callback) {
        this.status = FriendStatus.DECLINED;

        if (notification != null && notification.getId() != null)
            NotificationService.getInstance().delete(notification, callback);
    }

    public void removeFriend(@Nullable Notification notification, QueryCallback<Void> callback) {
        this.status = FriendStatus.REMOVED;

        if (notification != null && notification.getId() != null)
            NotificationService.getInstance().delete(notification, callback);
    }
}
