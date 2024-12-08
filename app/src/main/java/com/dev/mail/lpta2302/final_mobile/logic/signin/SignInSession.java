package com.dev.mail.lpta2302.final_mobile.logic.signin;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity(tableName = SignInSession.tableName)
public class SignInSession {
    @PrimaryKey @NonNull
    private String id;
    private String userId;

    public static final String tableName = "signInSessions";
}
