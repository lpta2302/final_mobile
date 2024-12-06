package com.dev.mail.lpta2302.final_mobile;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
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

    public void sendRequest() {
        this.status = FriendStatus.PENDING;
    }

    public void acceptRequest() {
        this.status = FriendStatus.ACCEPTED;
        createdAt = LocalDateTime.now();
    }

    public void declineRequest() {
        this.status = FriendStatus.DECLINED;
    }

    public void removeFriend() {
        this.status = FriendStatus.REMOVED;
    }

    public void save(ExpectationAndException onResult) {
        switch (status) {
            case DECLINED: case REMOVED:
                FriendService.instance.delete(this, onResult);

            default:
                if (id == null) FriendService.instance.create(this, onResult);
                else FriendService.instance.update(this, onResult);
        }
    }
}
