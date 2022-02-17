package com.example.fitnoise.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM UserProfile")
    UserProfile[] getUserProfile();

    @Query("SELECT * FROM UserProfile WHERE profileId = :id")
    UserProfile getUserProfileById(long id);

    @Insert
    long insertUserProfile(UserProfile userProfile);

    @Update
    void updateUserProfile(UserProfile userProfile);
}
