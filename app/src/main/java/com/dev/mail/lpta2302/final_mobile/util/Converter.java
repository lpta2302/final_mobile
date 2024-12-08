package com.dev.mail.lpta2302.final_mobile.util;

import androidx.room.TypeConverter;

import com.dev.mail.lpta2302.final_mobile.friend.FriendStatus;
import com.dev.mail.lpta2302.final_mobile.user.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp này để convert một số kiểu dữ liệu của Java thành kiểu có thể lưu được trong Room database và ngược lại.
 */
public class Converter {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date != null ? date.format(dateFormatter) : null;
    }

    @TypeConverter
    public static LocalDate toLocalDate(String date) {
        return date != null ? LocalDate.parse(date, dateFormatter) : null;
    }

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(dateTimeFormatter) : null;
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, dateTimeFormatter) : null;
    }

    @TypeConverter
    public static String fromGender(Gender gender) {
        return gender != null ? gender.name() : null;
    }

    @TypeConverter
    public static Gender toGender(String gender) {
        return gender != null ? Gender.valueOf(gender) : null;
    }

    @TypeConverter
    public static String fromFriendStatus(FriendStatus friendStatus) {
        return friendStatus != null ? friendStatus.name() : null;
    }

    @TypeConverter
    public static FriendStatus toFriendStatus(String friendStatus) {
        return friendStatus != null ? FriendStatus.valueOf(friendStatus) : null;
    }
}
