package com.dev.mail.lpta2302.final_mobile.friend;

import com.dev.mail.lpta2302.final_mobile.ExpectationAndException;
import com.dev.mail.lpta2302.final_mobile.Notification;
import com.dev.mail.lpta2302.final_mobile.NotificationService;
import com.dev.mail.lpta2302.final_mobile.user.User;
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

    public void sendRequest(ExpectationAndException onResult) {
        this.status = FriendStatus.PENDING;

        String message = user1.getFirstName() + " gửi cho bạn lời mời kết bạn.";
        Notification notification = new Notification(message, false, LocalDateTime.now(), user2);
        NotificationService.getInstance().create(notification, onResult);
    }

    public void acceptRequest(ExpectationAndException onResult) {
        this.status = FriendStatus.ACCEPTED;
        createdAt = LocalDateTime.now();

        String message = user2.getFirstName() + " chấp nhận lời mời kết bạn.";
        Notification notification = new Notification(message, false, LocalDateTime.now(), user1);
        NotificationService.getInstance().create(notification, onResult);
    }

    public void declineRequest() {
        this.status = FriendStatus.DECLINED;
    }

    public void removeFriend() {
        this.status = FriendStatus.REMOVED;
    }
}
