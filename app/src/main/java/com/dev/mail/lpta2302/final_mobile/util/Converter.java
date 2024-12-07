package com.dev.mail.lpta2302.final_mobile.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Converter {
    public static class ListConverter {
        @TypeConverter
        public static String fromList(List<String> list) {
            return new Gson().toJson(list);
        }

        @TypeConverter
        public static List<String> toList(String json) {
            Type type = new TypeToken<List<String>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
    }

    public static class DateConverter {
        @TypeConverter
        public static Long fromDate(Date date) {
            return date == null ? null : date.getTime();
        }
        @TypeConverter
        public static Date toDate(Long timestamp) {
            return timestamp == null ? null : new Date(timestamp);
        }
    }
}
