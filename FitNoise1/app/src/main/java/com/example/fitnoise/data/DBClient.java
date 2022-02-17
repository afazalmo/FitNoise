package com.example.fitnoise.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBClient {
    private static DatabaseFitnoise myDatabase;

    //public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static DatabaseFitnoise getDatabase(Context context){
        myDatabase = DatabaseFitnoise.getDatabase(context);
        return myDatabase;
    }

    // important: remember to run in different thread
    // todo: forget multi-threading for now, use the DAO


}
