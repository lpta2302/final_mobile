package com.dev.mail.lpta2302.final_mobile.post;

import com.dev.mail.lpta2302.final_mobile.comment.Comment;
import com.dev.mail.lpta2302.final_mobile.user.User;

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
public class Post {
    private String id;
    private String imageUrl;
    private String caption;
    private List<String> tags;
    private String authorId;
    private User author;
    private List<String> commentIds;
    private List<String> likes;
    private Date createdAt;
}
