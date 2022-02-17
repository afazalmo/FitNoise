package com.example.fitnoise.ui.events;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.data.WorkoutEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateWorkoutEventActivity extends AppCompatActivity {
    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;
    long eventId;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_workout_event);

        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");
        eventId = getIntent().getLongExtra("eventId", 0);

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
        // load data to update
        WorkoutEvent loadEvent = dbInstance.getWorkoutEventDao().getWorkoutEventById(eventId);

        // Find selected workout pos
        int workoutPos = 0;
        for (int x = 0; x < workoutList.length; x++){
            if (workoutList[x].workoutID == loadEvent.refWorkoutId){
                workoutPos = x;
                break;
            }
        }
        // Set existing data
        duration.setText(String.valueOf(loadEvent.durationMins));
        date = loadEvent.startDate;

        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", locale);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa", locale);
        displayDate.setText(dateFormatter.format(date));
        displayTime.setText(timeFormatter.format(date));

        ArrayAdapter<Workout> workoutEventArrayAdapter = new ArrayAdapter<Workout>(this, android.R.layout.simple_dropdown_item_1line, workoutList);
        workoutSpinner.setAdapter(workoutEventArrayAdapter);
        workoutSpinner.setSelection(workoutPos);


        // Date and Time Picker
        final View dialogView = View.inflate(this, R.layout.activity_set_date_time, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(dialogView);

        DatePicker datePicker = (DatePicker)dialogView.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker)dialogView.findViewById(R.id.time_picker);
        datePicker.updateDate(date.getYear() + 1900, date.getMonth(), date.getDay());
        timePicker.setHour(date.getHours());
        timePicker.setMinute(date.getMinutes());

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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update data
                Workout selectedWorkout = (Workout)workoutSpinner.getSelectedItem();
                String durationString = duration.getText().toString();

                // Validate form
                if (durationString.isEmpty() || selectedWorkout == null){
                    Toast.makeText(getApplicationContext(), "Missing field inputs!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Update data here
                    long workoutId = selectedWorkout.workoutID;
                    int durationInt = Integer.parseInt(durationString);

                    // Update data
                    loadEvent.durationMins = durationInt;
                    loadEvent.startDate = date;
                    loadEvent.refWorkoutId = workoutId;
                    dbInstance.getWorkoutEventDao().updateWorkoutEvent(loadEvent);

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