package com.dev.mail.lpta2302.final_mobile.logic.global;

import com.dev.mail.lpta2302.final_mobile.logic.friend.FriendService;
import com.dev.mail.lpta2302.final_mobile.logic.friend.Friendship;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

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
    private List<Friendship> friends = new ArrayList<>();
    public void createSession(User currentUser){
        setUser(currentUser);
        setAuthenticated(true);
        FriendService.getInstance().findAll(user, new QueryCallback<List<Friendship>>() {
            @Override
            public void onSuccess(List<Friendship> expectation) {
                friends = expectation;
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }
}
