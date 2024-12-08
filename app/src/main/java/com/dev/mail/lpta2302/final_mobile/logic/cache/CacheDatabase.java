package com.dev.mail.lpta2302.final_mobile.logic.cache;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dev.mail.lpta2302.final_mobile.logic.signin.SignInSession;
import com.dev.mail.lpta2302.final_mobile.logic.signin.SignInSessionDao;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserDao;
import com.dev.mail.lpta2302.final_mobile.logic.util.Converter;

/**
 * Lớp này dùng để migration với Room database.
 */
@Database(entities = {User.class, SignInSession.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class CacheDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract SignInSessionDao signInSessionDao();
}
