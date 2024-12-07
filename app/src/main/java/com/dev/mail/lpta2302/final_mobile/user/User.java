package com.dev.mail.lpta2302.final_mobile.user;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;

    public User(String email, String firstName, String lastName, Gender gender, LocalDate dateOfBirth) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
