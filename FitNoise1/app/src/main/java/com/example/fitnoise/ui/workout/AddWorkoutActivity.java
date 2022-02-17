package com.example.fitnoise.ui.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.ui.MainActivity;

public class AddWorkoutActivity extends AppCompatActivity {
    private EditText workoutName_;
    private Spinner exerciseSpinner;
    private EditText desc_;
    private EditText sets_;
    private EditText reps_;

    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");

        Button save = (Button) findViewById(R.id.saveButton);
        ImageButton close = (ImageButton) findViewById(R.id.closeButton);

        workoutName_ = (EditText) findViewById(R.id.WorkoutName);
        exerciseSpinner = (Spinner) findViewById(R.id.exerciseSpinner);
        desc_ = (EditText) findViewById(R.id.description);
        sets_ = (EditText) findViewById(R.id.setsId);
        reps_ = (EditText) findViewById(R.id.repsId);

        // Load Exercises
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        Exercise[] exercisesList = account.getAllExercises(this);

        ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<Exercise>(this, android.R.layout.simple_dropdown_item_1line, exercisesList);
        exerciseSpinner.setAdapter(exerciseAdapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workoutName = workoutName_.getText().toString();

                // Spinner should have Exercise as each item
                Exercise exercise = (Exercise)exerciseSpinner.getSelectedItem();

                for (int pos = 0; pos < exerciseAdapter.getCount(); pos++) {
                    exerciseSpinner.setSelection(pos);
                }

                String description = desc_.getText().toString();
                String sets = sets_.getText().toString();
                String reps = reps_.getText().toString();

                // Form validation
                if(workoutName.isEmpty() || description.isEmpty() || sets.isEmpty() || reps.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int setsVal = Integer.parseInt(sets);
                    int repsVal = Integer.parseInt(reps);

                    // Create workout in DB
                    Workout newWorkout = new Workout(workoutName, exercise.exerciseID, description, setsVal, repsVal);
                    long newWorkoutId = dbInstance.getWorkoutDao().insertWorkout(newWorkout);

                    // Update account with new workoutId ref
                    account.addWorkoutId(getApplicationContext(), newWorkoutId);
                    dbInstance.getUserAccountDao().updateUserAccount(account);
                    finish();

                    /*
                    Toast.makeText(getApplicationContext(), "Name: " + workoutName + "\n" +
                            "Exercise: " + exercise + "\n" +
                            "Description: " + description + "\n" +
                            "Sets: " + setsVal + "\n" +
                            "Reps: " + repsVal, Toast.LENGTH_LONG).show();
                    */


                }
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}