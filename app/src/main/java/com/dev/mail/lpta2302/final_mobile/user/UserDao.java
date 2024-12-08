package com.dev.mail.lpta2302.final_mobile.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Update
    void update(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE id = :id")
    User findById(String id);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM users")
    List<User> findAll();
}
