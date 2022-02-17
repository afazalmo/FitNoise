package com.example.fitnoise.data;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class UserAccount {
    @PrimaryKey(autoGenerate = true)
    public long accountID;
    public String username;
    public String password;
    public String email;
    public long profileId; // profiles will be created on account creation

    public long[] workoutSet;
    public long[] exerciseSet;
    public long[] workoutEventSet;

    @Ignore
    public UserAccount(){
        this.accountID = 0;
        this.username = "";
        this.password = "";
        this.email = "";
        this.profileId = 0;
        this.workoutSet = null;
        this.exerciseSet = null;
        this.workoutEventSet = null;
    }

    @Ignore
    public UserAccount(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileId = 0;
        this.workoutSet = null;
        this.exerciseSet = null;
        this.workoutEventSet = null;
    }

    // Room Constructor
    public UserAccount(long accountID, String username, String password, String email, long profileId, long[] workoutSet, long[] exerciseSet, long[] workoutEventSet) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileId = profileId;
        this.workoutSet = workoutSet;
        this.exerciseSet = exerciseSet;
        this.workoutEventSet = workoutEventSet;
    }

    @Ignore
    // Used to set profileId when account created
    public void setProfileId(long id){
        this.profileId = id;
    }

    @Ignore
    // Get saved workouts list
    public Workout[] getAllWorkouts(Context context){
        if (workoutSet == null){
            return new Workout[]{};
        } else {
            Workout[] workouts = DatabaseFitnoise.getDatabase(context).getWorkoutDao().getWorkoutByIds(workoutSet);
            return workouts;
        }
    }

    @Ignore
    // Get saved exercises list
    public Exercise[] getAllExercises(Context context){
        if (exerciseSet == null){
            return new Exercise[]{};
        } else {
            Exercise[] exercises = DatabaseFitnoise.getDatabase(context).getExerciseDao().getExerciseByIds(exerciseSet);
            return exercises;
        }
    }

    @Ignore
    // Get saved exercises list
    public WorkoutEvent[] getAllEvents(Context context){
        if (workoutEventSet == null){
            return new WorkoutEvent[]{};
        } else {
            WorkoutEvent[] events = DatabaseFitnoise.getDatabase(context).getWorkoutEventDao().getWorkoutEventByIds(workoutEventSet);
            return events;
        }
    }

    @Ignore
    // Get saved profile
    public UserProfile getProfile(Context context){
        // profileId will always be valid
        UserProfile profile = DatabaseFitnoise.getDatabase(context).getUserProfileDao().getUserProfileById(this.profileId);
        return profile;
    }

    @Ignore
    public void addExerciseId(Context context, long id){
        if (exerciseSet == null){
            exerciseSet = new long[]{id};
        }
        else {
            // Convert to ArrayList
            ArrayList<Long> exerciseSetArray = new ArrayList<>(exerciseSet.length);
            for (long i: exerciseSet){
                exerciseSetArray.add(i);
            }

            // Update new exerciseId
            exerciseSetArray.add(id);

            // Convert back to int[]
            long[] exerciseArray = new long[exerciseSetArray.size()];
            for (int x = 0; x < exerciseArray.length; x++){
                exerciseArray[x] = exerciseSetArray.get(x);
            }
            // Save
            exerciseSet = exerciseArray;
        }
    }

    @Ignore
    public void addWorkoutId(Context context, long id) {
        if (workoutSet == null) {
            workoutSet = new long[]{id};
        } else {
            // Convert to ArrayList
            ArrayList<Long> workoutSetArray = new ArrayList<>(workoutSet.length);
            for (long i : workoutSet) {
                workoutSetArray.add(i);
            }

            // Add into arrayList
            workoutSetArray.add(id);

            // Convert back to int[]
            long[] workoutArray = new long[workoutSetArray.size()];
            for (int x = 0; x < workoutArray.length; x++) {
                workoutArray[x] = workoutSetArray.get(x);
            }
            // Save
            workoutSet = workoutArray;
        }
    }

    // Remove exerciseId
    @Ignore
    public void removeExerciseId(Context context, long id) {
        if (exerciseSet != null) {
            // Convert to ArrayList
            ArrayList<Long> exerciseSetArray = new ArrayList<>(exerciseSet.length);

            for (long i : exerciseSet) {
                exerciseSetArray.add(i);
            }

            // remove id from arrayList
            for (int x = 0; x < exerciseSetArray.size(); x++){
                if (exerciseSetArray.get(x) == id){
                    exerciseSetArray.remove(x);
                    break;
                }
            }

            // Convert back to long[]
            long[] exerciseArray = new long[exerciseSetArray.size()];
            for (int x = 0; x < exerciseArray.length; x++) {
                exerciseArray[x] = exerciseSetArray.get(x);
            }
            // Save
            exerciseSet = exerciseArray;
        }
    }

    // Remove workoutId
    @Ignore
    public void removeWorkoutId(Context context, long id) {
        if (workoutSet != null) {
            // Convert to ArrayList
            ArrayList<Long> workoutSetArray = new ArrayList<>(workoutSet.length);

            for (long i : workoutSet) {
                workoutSetArray.add(i);
            }

            // remove id from arrayList
            for (int x = 0; x < workoutSetArray.size(); x++){
                if (workoutSetArray.get(x) == id){
                    workoutSetArray.remove(x);
                    break;
                }
            }

            // Convert back to long[]
            long[] workoutArray = new long[workoutSetArray.size()];
            for (int x = 0; x < workoutArray.length; x++) {
                workoutArray[x] = workoutSetArray.get(x);
            }
            // Save
            workoutSet = workoutArray;
        }
    }

    @Ignore
    public void addEventId(Context context, long id){
        if (workoutEventSet == null){
            workoutEventSet = new long[]{id};
        }
        else {
            // Convert to ArrayList
            ArrayList<Long> workoutEventSetArray = new ArrayList<>(workoutEventSet.length);
            for (long i: workoutEventSet){
                workoutEventSetArray.add(i);
            }

            // Update new exerciseId
            workoutEventSetArray.add(id);

            // Convert back to int[]
            long[] workoutEventArray = new long[workoutEventSetArray.size()];
            for (int x = 0; x < workoutEventArray.length; x++){
                workoutEventArray[x] = workoutEventSetArray.get(x);
            }
            // Save
            workoutEventSet = workoutEventArray;
        }
    }

    @Ignore
    public void removeEventId(Context context, long id) {
        if (workoutEventSet != null) {
            // Convert to ArrayList
            ArrayList<Long> workoutEventSetArray = new ArrayList<>(workoutEventSet.length);

            for (long i : workoutEventSet) {
                workoutEventSetArray.add(i);
            }

            // remove id from arrayList
            for (int x = 0; x < workoutEventSetArray.size(); x++){
                if (workoutEventSetArray.get(x) == id){
                    workoutEventSetArray.remove(x);
                    break;
                }
            }

            // Convert back to long[]
            long[] workoutEventArray = new long[workoutEventSetArray.size()];
            for (int x = 0; x < workoutEventArray.length; x++) {
                workoutEventArray[x] = workoutEventSetArray.get(x);
            }
            // Save
            workoutEventSet = workoutEventArray;
        }
    }

}
