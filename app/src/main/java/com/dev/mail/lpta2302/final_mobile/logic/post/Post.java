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
public class Post implements Parcelable{
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(caption);
        dest.writeStringList(tags);
        dest.writeString(authorId);
        dest.writeParcelable(author, flags);
        dest.writeStringList(commentIds);
        dest.writeStringList(likes);
        dest.writeLong(createdAt != null ? createdAt.getTime() : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
