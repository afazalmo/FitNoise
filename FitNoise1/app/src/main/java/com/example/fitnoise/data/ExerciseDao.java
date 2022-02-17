package com.example.fitnoise.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ExerciseDao {


    //@Query("SELECT * FROM Exercise")
    //Exercise[] getAllExercise();

    @Query("SELECT * FROM Exercise WHERE exerciseID = :id")
    Exercise getExerciseById(long id);

    @Query("SELECT * FROM Exercise WHERE exerciseID IN(:ids)")
    Exercise[] getExerciseByIds(long[] ids);

    @Insert
    long insertExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);
}
