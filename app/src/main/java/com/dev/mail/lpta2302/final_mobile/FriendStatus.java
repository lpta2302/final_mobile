package com.dev.mail.lpta2302.final_mobile;

public enum FriendStatus {
    ACCEPTED,
    DECLINED,
    PENDING,
    REMOVED;

    public static boolean canParseEnum(String value) {
        try {
            Enum.valueOf(FriendStatus.class, value);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
