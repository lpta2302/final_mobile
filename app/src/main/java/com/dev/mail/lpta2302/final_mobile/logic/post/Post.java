package com.dev.mail.lpta2302.final_mobile.logic.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.util.TimeFormatter;

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
public class Post{
    private String id;
    private String imageUrl;
    private String caption;
    private List<String> tags = new ArrayList<>();
    private String authorId;
    private User author;
    private List<String> commentIds = new ArrayList<>();
    private List<String> likes = new ArrayList<>();
    private Date createdAt;

    public String formatedDate(){
        return TimeFormatter.timeAgo(createdAt);
    }

    // Parcelable implementation
    protected Post(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        caption = in.readString();
        tags = in.createStringArrayList();
        authorId = in.readString();
        author = in.readParcelable(User.class.getClassLoader());
        commentIds = in.createStringArrayList();
        likes = in.createStringArrayList();
        createdAt = new Date(in.readLong());
    }
}
