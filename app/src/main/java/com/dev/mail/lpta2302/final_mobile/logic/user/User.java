package com.dev.mail.lpta2302.final_mobile.logic.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = User.tableName)
public class User{
    @PrimaryKey
    @NonNull
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String avatar;
    private LocalDate dateOfBirth; // Use String for Parcelable

    public static final String tableName = "users";

    public User(String email, String firstName, String lastName, String avatar, Gender gender, LocalDate dateOfBirth) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.avatar = avatar;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String toStringDateOfBirth(){
        return dateOfBirth.toString();
    }

    public LocalDate fromStringDateOfBirth(String date){
        return  LocalDate.parse(date);
    }

    // Parcelable implementation
    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        gender = Gender.valueOf(in.readString());
        dateOfBirth = fromStringDateOfBirth(in.readString());
    }
}
