package com.dev.mail.lpta2302.final_mobile.Post;

import com.dev.mail.lpta2302.final_mobile.Comment.Comment;
import com.dev.mail.lpta2302.final_mobile.User;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private String id;
    private String imageUrl;
    private String caption;
    private List<String> tags;
    private User author;
    private List<Comment> comments;
    private List<User> likes;
    private Date createdAt;
}
