package com.example.fitnoise.data;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Workout {
    @PrimaryKey(autoGenerate = true)
    public long workoutID;
    public String name;
    public long exerciseId; // will always be valid
    public String description;
    public int sets;
    public int reps;

    @Ignore
    public Workout() {
        this.workoutID = 0;
        this.name = "";
        this.exerciseId = 0;
        this.description = "";
        this.sets = 0;
        this.reps = 0;
    }

    @Ignore
    public Workout(String name, long exerciseId, String description, int sets, int reps) {
        this.workoutID = 0;
        this.name = name;
        this.exerciseId = exerciseId;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
    }


    public Workout(long workoutID, String name, long exerciseId, String description, int sets, int reps) {
        this.workoutID = workoutID;
        this.name = name;
        this.exerciseId = exerciseId;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
    }


    public Exercise getExercise(Context context){
        if (exerciseId == 0){
            Exercise noExercise = new Exercise();
            noExercise.name = "Deleted Exercise";
            noExercise.description = "The referenced exercise has been deleted";
            return noExercise;
        }
        else {
            Exercise exercise = DatabaseFitnoise.getDatabase(context).getExerciseDao().getExerciseById(exerciseId);
            return exercise;
        }
    }

    // The toString method is extremely important to making this class work with a Spinner
    // (or ListView) object because this is the method called when it is trying to represent
    // this object within the control.  If you do not have a toString() method, you WILL
    // get an exception.
    public String toString()
    {
        return name;
    }

}