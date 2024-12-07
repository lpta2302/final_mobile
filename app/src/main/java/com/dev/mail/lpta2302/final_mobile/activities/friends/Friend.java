package com.dev.mail.lpta2302.final_mobile.activities.friends;

public class Friend {
    private String name;
    private int avatarResId; // Resource ID của ảnh đại diện

    public Friend(String name, int avatarResId) {
        this.name = name;
        this.avatarResId = avatarResId;
    }

    public String getName() {
        return name;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}

