package com.example.fitnoise.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WorkoutEventDao {

    @Query("SELECT * FROM WorkoutEvent")
    WorkoutEvent getAllWorkoutEvent();

    @Query("SELECT * FROM WorkoutEvent WHERE workoutEventID = :id")
    WorkoutEvent getWorkoutEventById(long id);

    @Query("SELECT * FROM WorkoutEvent WHERE workoutEventID IN(:ids)")
    WorkoutEvent[] getWorkoutEventByIds(long[] ids);

    @Insert
    long insertWorkoutEvent(WorkoutEvent event);

    @Update
    void updateWorkoutEvent(WorkoutEvent event);

    @Update
    void updateAllWorkoutEvents(WorkoutEvent[] eventList);

    @Delete
    void deleteWorkoutEvent(WorkoutEvent event);

}
