package com.example.fitnoise.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
        UserAccount.class,
        UserProfile.class,
        Exercise.class,
        Workout.class,
        WorkoutEvent.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseFitnoise extends RoomDatabase {
    private static DatabaseFitnoise INSTANCE;
    public static boolean initialLoad = true;
    public abstract UserAccountDao getUserAccountDao();
    public abstract UserProfileDao getUserProfileDao();
    public abstract ExerciseDao getExerciseDao();
    public abstract WorkoutDao getWorkoutDao();
    public abstract WorkoutEventDao getWorkoutEventDao();


    public static DatabaseFitnoise getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseFitnoise.class, "fitnoise-DB")
                // allow queries on the main thread.
                // Don't do this on a real app! See PersistenceBasicSample for an example.
                .allowMainThreadQueries()
                    .build();


        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

}
