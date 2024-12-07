package com.dev.mail.lpta2302.final_mobile.post;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.util.Converter.ListConverter;
import com.dev.mail.lpta2302.final_mobile.util.Converter.DateConverter;
import com.dev.mail.lpta2302.final_mobile.util.TimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @PrimaryKey
    private String id;
    private String imageUrl;
    private String caption;
    @TypeConverters(ListConverter.class)
    private List<String> tags;
    private String authorId;
    private User author;
    @TypeConverters(ListConverter.class)
    private List<String> commentIds;
    @TypeConverters(ListConverter.class)
    private List<String> likes = new ArrayList<>();
    @TypeConverters(DateConverter.class)
    private Date createdAt;
    public int getTotalLike(){
        return likes.size();
    }
    public int getTotalComment(){
        return commentIds.size();
    }
    public String getFormatedDate(){
        return TimeFormatter.timeAgo(createdAt);
    }
}
