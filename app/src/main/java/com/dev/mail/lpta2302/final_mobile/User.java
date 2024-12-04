package com.dev.mail.lpta2302.final_mobile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class User {
    private String id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}
