package com.dev.mail.lpta2302.final_mobile.global;

import com.dev.mail.lpta2302.final_mobile.user.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthUser {
    private AuthUser(){}
    @Getter
    public static final AuthUser instance = new AuthUser();
    private User user;
    private boolean isAuthenticated = false;

}
