package com.example.fitnoise.ui.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;

public class DisplayWorkoutActivity extends AppCompatActivity {
    //TextView id;
    EditText name, desc, sets, reps;
    Spinner exercise;
    Button closeBtn, updateBtn;

    DatabaseFitnoise dbInstance;
    UserSession session;
    long workoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout);

        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");
        workoutID = getIntent().getLongExtra("workoutId", 0);

        // Widgets
        //id = (TextView) findViewById(R.id.wNum);
        name = (EditText) findViewById(R.id.wName);
        exercise = (Spinner) findViewById(R.id.wExercise);
        desc = (EditText) findViewById(R.id.wDescription);
        sets = (EditText) findViewById(R.id.wSets);
        reps = (EditText) findViewById(R.id.wReps);
        closeBtn = (Button) findViewById(R.id.display_close_button);
        updateBtn = (Button) findViewById(R.id.updateWorkoutBtn);

        // Find exercise in DB
        if (workoutID == 0){
            Toast.makeText(this, "Fail to load workout", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // Load objects from DB
            Workout myWorkout = dbInstance.getWorkoutDao().getWorkoutById(workoutID);
            Exercise myExercise = myWorkout.getExercise(this);


            // Load Exercise List
            UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
            Exercise[] exercisesList = account.getAllExercises(this);
            ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<Exercise>(this, android.R.layout.simple_dropdown_item_1line, exercisesList);
            exercise.setAdapter(exerciseAdapter);


            // Find selected exercise pos
            int exercisePos = 0;
            for (int x = 0; x < exercisesList.length; x++){
                if (exercisesList[x].exerciseID == myWorkout.exerciseId){
                    exercisePos = x;
                    break;
                }
            }

            // Update display
            //id.setText(String.valueOf(myWorkout.workoutID));
            name.setText(myWorkout.name);
            exercise.setSelection(exercisePos);
            desc.setText(myWorkout.description);
            sets.setText(String.valueOf(myWorkout.sets));
            reps.setText(String.valueOf(myWorkout.reps));

            // Close Button
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            // Update Button
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newWorkoutName = name.getText().toString();

                    // Spinner should have Exercise as each item
                    Exercise selectedExercise = (Exercise)exercise.getSelectedItem();

                    /*
                    for (int pos = 0; pos < exerciseAdapter.getCount(); pos++) {
                        exerciseSpinner.setSelection(pos);
                    }*/

                    String nDescription = desc.getText().toString();
                    String nSets = sets.getText().toString();
                    String nReps = reps.getText().toString();

                    // Form validation
                    if(newWorkoutName.isEmpty() || nDescription.isEmpty() || nSets.isEmpty() || nReps.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int setsVal = Integer.parseInt(nSets);
                        int repsVal = Integer.parseInt(nReps);

                        // Update workout, save to DB
                        myWorkout.name = newWorkoutName;
                        myWorkout.exerciseId = selectedExercise.exerciseID;
                        myWorkout.description = nDescription;
                        myWorkout.sets = setsVal;
                        myWorkout.reps = repsVal;
                        dbInstance.getWorkoutDao().updateWorkout(myWorkout);

                        finish();
                    }

                }
            });

        }
    }
}