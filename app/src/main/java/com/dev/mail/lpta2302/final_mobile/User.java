package com.dev.mail.lpta2302.final_mobile;


import org.springframework.security.crypto.bcrypt.BCrypt;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
    @Setter
    private String id;
    @Setter
    private String email;
    private String password;
    @Setter
    private String firstName;
    @Setter
    private String lastName;

    public User() {}
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public void getFriendships(int limit, int page, ExpectationAndException onResult) {
        FriendService.instance.findAll(this, limit, page, onResult);
    }

    public void save(ExpectationAndException onResult) {
        if (id == null) UserRepository.instance.create(this, onResult);
        else UserRepository.instance.update(this, onResult);
    }

    public String toString() {
        return id + email + firstName + lastName;
    }
}
