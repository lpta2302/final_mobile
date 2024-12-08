package com.dev.mail.lpta2302.final_mobile.logic.signin;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SignInSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SignInSession session);

    @Query("SELECT * FROM signInSessions WHERE id = :id")
    SignInSession findById(String id);

    @Query("DELETE FROM signInSessions")
    void deleteAll();
}
