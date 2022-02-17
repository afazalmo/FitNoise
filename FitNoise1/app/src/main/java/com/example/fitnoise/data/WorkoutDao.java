package com.example.fitnoise.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WorkoutDao {


    //@Query("SELECT * FROM Workout")
    //Workout[] getAllWorkouts();

    @Query("SELECT * FROM Workout WHERE workoutID = :id")
    Workout getWorkoutById(long id);

    @Query("SELECT * FROM Workout WHERE workoutID IN(:ids)")
    Workout[] getWorkoutByIds(long[] ids);

    @Insert
    long insertWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Update
    void updateAllWorkouts(Workout[] workoutList);

    @Delete
    void deleteWorkout(Workout workout);

}
