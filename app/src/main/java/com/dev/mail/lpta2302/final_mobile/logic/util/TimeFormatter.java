package com.dev.mail.lpta2302.final_mobile.logic.util;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {
    @NonNull
    public static String timeAgo(@NonNull Date createdDate) {
        Date now = new Date();
        long duration = now.getTime() - createdDate.getTime();

        if (duration < TimeUnit.MINUTES.toMillis(1)) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            return seconds <= 1 ? "1 second ago" : seconds + " seconds ago";
        } else if (duration < TimeUnit.HOURS.toMillis(1)) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else if (duration < TimeUnit.DAYS.toMillis(1)) {
            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(duration);
            return days == 1 ? "1 day ago" : days + " days ago";
        }
    }
}
