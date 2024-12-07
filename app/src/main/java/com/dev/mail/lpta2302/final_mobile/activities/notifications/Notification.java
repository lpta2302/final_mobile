package com.dev.mail.lpta2302.final_mobile.activities.notifications;

public class Notification {
    private String name;
    private int avatarResId; // Resource ID của ảnh đại diện
    private String notification;

    public Notification(String name, int avatarResId, String notification) {
        this.name = name;
        this.avatarResId = avatarResId;
        this.notification = notification;
    }

    public String getName() {
        return name;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public String getNotification() { return notification; }
}
