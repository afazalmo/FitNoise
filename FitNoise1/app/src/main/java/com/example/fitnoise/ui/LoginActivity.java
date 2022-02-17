package com.example.fitnoise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DBClient;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserProfile;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.data.WorkoutEvent;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    DatabaseFitnoise dbInstance;

    private Button login, signUp, forgotPassword;
    private EditText userName, password;
    //public static final String TABLE_NAME="userInfo_table";
    private static final int WAIT_TIME = 3 * 60 * 1000;
    private int loginAttempts = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbInstance = DBClient.getDatabase(getApplicationContext());
        loadTestData();
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signup);
        userName = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pass);
        forgotPassword = (Button) findViewById(R.id.forgotpass);


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Check login attempts
                if (loginAttempts == 0) {
                    Toast.makeText(LoginActivity.this, "Your attempt has reach 0, please wait 3 minutes to login again", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean found = false;
                String user = userName.getText().toString();
                String pass = password.getText().toString();

                // Create user session
                UserSession session = new UserSession();

                // Pull accounts from DB
                UserAccount[] accounts = dbInstance.getUserAccountDao().getAllUserAccounts();
                for (int x = 0; x < accounts.length; x++){
                    if (accounts[x].username.equals(user) && accounts[x].password.equals(pass)) {
                        session = new UserSession(accounts[x].accountID, accounts[x].username);
                        found = true;
                        break;
                    }
                }

                // Perform login
                if (found){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("session", session);
                    startActivity(intent);
                }
                else {
                    loginAttempts--;
                    Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                    if (loginAttempts >= 0) {
                        // Run timed thread to refresh login attempts
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loginAttempts = 5;
                            }
                        }, WAIT_TIME);
                    }
                }
             }
        });

        //redirect to signup pages
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        // Forgot password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(in);
            }
        });
    }

    public void loadTestData(){
        if (DatabaseFitnoise.initialLoad){

            // Test account with pre-populated data
            long profileId = dbInstance.getUserProfileDao().insertUserProfile(new UserProfile(
                    "John", "Smith", 20, "Male", 100, 100, 50, 50, 50
            ));
            UserAccount newAccount = new UserAccount(
                    "admin", "admin", "admin@example.com"
            );
            newAccount.setProfileId(profileId);
            long accountId = dbInstance.getUserAccountDao().insertUserAccount(newAccount);
            UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(accountId);

            // Add sample exercises
            Exercise exercise1 = new Exercise("Pushups", "Use two hands and push up against the ground", "");
            Exercise exercise2 = new Exercise("Situps", "Sit down on the ground, then pull your up into the sitting position.", "");
            Exercise exercise3 = new Exercise("Lift Weights", "Lift 10lb weights", "");
            Exercise exercise4 = new Exercise("Running", "Run for at least 1km", "");
            Exercise exercise5 = new Exercise("Basic Stretches", "Do some warm up stretches", "");
            long eId1 = dbInstance.getExerciseDao().insertExercise(exercise1);
            long eId2 = dbInstance.getExerciseDao().insertExercise(exercise2);
            long eId3 = dbInstance.getExerciseDao().insertExercise(exercise3);
            long eId4 = dbInstance.getExerciseDao().insertExercise(exercise4);
            long eId5 = dbInstance.getExerciseDao().insertExercise(exercise5);
            account.addExerciseId(getApplicationContext(), eId1);
            account.addExerciseId(getApplicationContext(), eId2);
            account.addExerciseId(getApplicationContext(), eId3);
            account.addExerciseId(getApplicationContext(), eId4);
            account.addExerciseId(getApplicationContext(), eId5);

            // Add sample workouts
            Workout workout1 = new Workout("Morning Workout", eId1, "A light workout in the morning", 1, 2);
            Workout workout2 = new Workout("Daily Workout", eId4, "Everyday workout", 1, 1);
            Workout workout3 = new Workout("Weekend Workout", eId2, "A more intense workout", 1, 2);
            Workout workout4 = new Workout("Light Workout", eId5, "Simple workout for anytime", 1, 2);
            long wId1 = dbInstance.getWorkoutDao().insertWorkout(workout1);
            long wId2 = dbInstance.getWorkoutDao().insertWorkout(workout2);
            long wId3 = dbInstance.getWorkoutDao().insertWorkout(workout3);
            long wId4 = dbInstance.getWorkoutDao().insertWorkout(workout4);
            account.addWorkoutId(getApplicationContext(), wId1);
            account.addWorkoutId(getApplicationContext(), wId2);
            account.addWorkoutId(getApplicationContext(), wId3);
            account.addWorkoutId(getApplicationContext(), wId4);

            // Add sample workout events
            WorkoutEvent event1 = new WorkoutEvent(new Date(2020-1900, 8, 1, 7, 0), 60, wId1);
            WorkoutEvent event2 = new WorkoutEvent(new Date(2020-1900, 8, 3, 17, 0), 50, wId2);
            WorkoutEvent event3 = new WorkoutEvent(new Date(2020-1900, 8, 7, 7, 30), 90, wId3);
            WorkoutEvent event4 = new WorkoutEvent(new Date(2020-1900, 8, 10, 14, 0), 20, wId4);
            WorkoutEvent event5 = new WorkoutEvent(new Date(2020-1900, 8, 16, 16, 30), 50, wId4);
            long eventId1 = dbInstance.getWorkoutEventDao().insertWorkoutEvent(event1);
            long eventId2 = dbInstance.getWorkoutEventDao().insertWorkoutEvent(event2);
            long eventId3 = dbInstance.getWorkoutEventDao().insertWorkoutEvent(event3);
            long eventId4 = dbInstance.getWorkoutEventDao().insertWorkoutEvent(event4);
            long eventId5 = dbInstance.getWorkoutEventDao().insertWorkoutEvent(event5);
            account.addEventId(getApplicationContext(), eventId1);
            account.addEventId(getApplicationContext(), eventId2);
            account.addEventId(getApplicationContext(), eventId3);
            account.addEventId(getApplicationContext(), eventId4);
            account.addEventId(getApplicationContext(), eventId5);


            // Update account
            dbInstance.getUserAccountDao().updateUserAccount(account);
            DatabaseFitnoise.initialLoad = false;
        }
    }

}
