package com.dev.mail.lpta2302.final_mobile.Post;

import com.dev.mail.lpta2302.final_mobile.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchParam {
    private String caption;
    private User user;
}
