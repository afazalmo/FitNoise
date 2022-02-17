package com.example.fitnoise.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserAccountDao {

    @Query("SELECT * FROM UserAccount")
    UserAccount[] getAllUserAccounts();

    @Query("SELECT * FROM UserAccount WHERE accountID = :id")
    UserAccount getUserAccountById(long id);

    @Insert
    long insertUserAccount(UserAccount userAccount);

    @Update
    void updateUserAccount(UserAccount userAccount);
}
