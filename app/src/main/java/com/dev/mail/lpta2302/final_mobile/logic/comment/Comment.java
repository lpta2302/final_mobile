package com.dev.mail.lpta2302.final_mobile.logic.comment;

import com.dev.mail.lpta2302.final_mobile.logic.user.User;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String content;
    private String authorId;
    private User author;
    private Date createdAt;
}
