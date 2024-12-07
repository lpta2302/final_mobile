package com.dev.mail.lpta2302.final_mobile.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import com.dev.mail.lpta2302.final_mobile.util.Converter.DateConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @PrimaryKey
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    @TypeConverters(DateConverter.class)
    private Date dateOfBirth;

    public User(String email, String firstName, String lastName, Gender gender, Date dateOfBirth) {
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
