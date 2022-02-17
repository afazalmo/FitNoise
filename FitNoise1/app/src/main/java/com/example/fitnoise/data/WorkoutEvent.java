package com.example.fitnoise.data;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class WorkoutEvent {
    @PrimaryKey(autoGenerate = true)
    public long workoutEventID;
    public Date startDate;
    public int durationMins;
    public long refWorkoutId;

    @Ignore
    public WorkoutEvent(){
        this.workoutEventID = 0;
        this.startDate = null;
        this.durationMins = 0;
        this.refWorkoutId = 0;
    }

    @Ignore
    public WorkoutEvent(Date startDate, int durationMins, long refWorkoutId) {
        this.workoutEventID = 0;
        this.startDate = startDate;
        this.durationMins = durationMins;
        this.refWorkoutId = refWorkoutId;
    }

    // Room constructor
    public WorkoutEvent(long workoutEventID, Date startDate, int durationMins, long refWorkoutId) {
        this.workoutEventID = workoutEventID;
        this.startDate = startDate;
        this.durationMins = durationMins;
        this.refWorkoutId = refWorkoutId;
    }

    public Workout getWorkout(Context context){
        if (refWorkoutId == 0){
            Workout noWorkout = new Workout();
            noWorkout.name = "Deleted Workout";
            noWorkout.description = "The referenced Workout has been deleted";
            return noWorkout;
        }
        else {
            Workout selectedWorkout = DatabaseFitnoise.getDatabase(context).getWorkoutDao().getWorkoutById(refWorkoutId);
            return selectedWorkout;
        }

    }


}
