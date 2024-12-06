package com.dev.mail.lpta2302.final_mobile;

import com.dev.mail.lpta2302.final_mobile.user.User;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String id;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private User recipient;

    public Notification(String message, Boolean isRead, LocalDateTime createdAt, User recipient) {
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.recipient = recipient;
    }
}
