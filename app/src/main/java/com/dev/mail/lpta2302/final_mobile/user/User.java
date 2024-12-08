package com.dev.mail.lpta2302.final_mobile.user;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Parcelable {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String dateOfBirth; // Use String for Parcelable

    public User(String email, String firstName, String lastName, Gender gender, String dateOfBirth) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Parcelable implementation
    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        gender = Gender.valueOf(in.readString());
        dateOfBirth = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(gender.name());
        dest.writeString(dateOfBirth);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
