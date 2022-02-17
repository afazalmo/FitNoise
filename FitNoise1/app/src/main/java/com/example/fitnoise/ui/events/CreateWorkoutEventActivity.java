package com.example.fitnoise.ui.events;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DBClient;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.data.WorkoutEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateWorkoutEventActivity extends AppCompatActivity {
    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout_event);


        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");

        // Widgets
        ImageButton closeBtn = (ImageButton)findViewById(R.id.closeButton);
        Button saveBtn = (Button) findViewById(R.id.saveButton);
        Spinner workoutSpinner = (Spinner) findViewById(R.id.workoutSpinner);
        EditText duration = (EditText)findViewById(R.id.duration);
        Button setDateTimeBtn = (Button) findViewById(R.id.set_date_time);
        TextView displayDate = (TextView) findViewById(R.id.displayDate);
        TextView displayTime = (TextView) findViewById(R.id.displayTime);

        // Load data
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        Workout[] workoutList = account.getAllWorkouts(this);

        ArrayAdapter<Workout> workoutEventArrayAdapter = new ArrayAdapter<Workout>(this, android.R.layout.simple_dropdown_item_1line, workoutList);
        workoutSpinner.setAdapter(workoutEventArrayAdapter);


        // Set Default Date
        date = new Date();
        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", locale);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa", locale);
        displayDate.setText(dateFormatter.format(date));
        displayTime.setText(timeFormatter.format(date));

        // Date and Time Picker
        final View dialogView = View.inflate(this, R.layout.activity_set_date_time, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(dialogView);

        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        // Listeners
        // Toggle Date/Time
        dialogView.findViewById(R.id.switchDateTimeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePicker.getVisibility() == View.VISIBLE){
                    datePicker.setVisibility(View.INVISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                }
                else {
                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.INVISIBLE);
                }

            }
        });
        // Save Date/Time
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set date
                date = new Date(datePicker.getYear() - 1900,
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                alertDialog.dismiss();
                // Update display
                displayDate.setText(dateFormatter.format(date));
                displayTime.setText(timeFormatter.format(date));
                Toast.makeText(getApplicationContext(), "Date and Time Set!", Toast.LENGTH_SHORT);
            }});


        setDateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        // Save Event
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save data
                Workout selectedWorkout = (Workout)workoutSpinner.getSelectedItem();
                String durationString = duration.getText().toString();

                // Validate form
                if (durationString.isEmpty() || selectedWorkout == null){
                    Toast.makeText(getApplicationContext(), "Missing field inputs!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Save data here
                    long workoutId = selectedWorkout.workoutID;
                    int durationInt = Integer.parseInt(durationString);
                    // save to db
                    WorkoutEvent newEvent = new WorkoutEvent(date, durationInt, workoutId);
                    long eventId = dbInstance.getWorkoutEventDao().insertWorkoutEvent(newEvent);
                    // add event ref
                    UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
                    account.addEventId(getApplicationContext(), eventId);
                    dbInstance.getUserAccountDao().updateUserAccount(account);
                    finish();
                }

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}