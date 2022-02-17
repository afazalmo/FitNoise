package com.example.fitnoise.ui.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.data.WorkoutEvent;
import com.example.fitnoise.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends BaseAdapter {
    View view;
    Context context;
    ArrayList<WorkoutEvent> eventList;
    LayoutInflater inflater;
    UserSession session;
    DatabaseFitnoise dbInstance;


    public EventAdapter(Context appContext, ArrayList<WorkoutEvent> eventList, UserSession session){
        this.context = appContext;
        this.eventList = eventList;
        inflater = LayoutInflater.from(appContext);
        this.session = session;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int pos, View parentView, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.workout_event_item, null, true);
        dbInstance = DatabaseFitnoise.getDatabase(view.getContext());

        TextView dateTime = view.findViewById(R.id.eventDateTime);
        TextView eventName = view.findViewById(R.id.eventWorkoutName);
        Button startBtn = view.findViewById(R.id.eventStartBtn);
        Button deleteBtn = view.findViewById(R.id.deleteEventBtn);
        LinearLayout eventItem = view.findViewById(R.id.eventItem);

        // Load objects
        WorkoutEvent myWorkoutEvent = eventList.get(pos);
        Workout myWorkout = eventList.get(pos).getWorkout(view.getContext());

        // If reference workout does not exist
        if (myWorkout.workoutID == 0){
            startBtn.setEnabled(false);
            startBtn.setText("disabled");
        }


        // Update display
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy\nhh:mmaa");
        //String durationString = String.valueOf(myWorkoutEvent.durationMins);

        String workoutNameString = myWorkout.name;
        String dateString = sdf.format(myWorkoutEvent.startDate);

        dateTime.setText(dateString);
        eventName.setText(workoutNameString);

        eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edit event item
                //Toast.makeText(view.getContext(), "Clicked on " + eventList.get(pos).getWorkout(view.getContext()).name, Toast.LENGTH_SHORT).show();
                Intent updateIntent = new Intent(view.getContext(), UpdateWorkoutEventActivity.class);
                updateIntent.putExtra("session", session);
                updateIntent.putExtra("eventId", eventList.get(pos).workoutEventID);
                view.getContext().startActivity(updateIntent);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: start workout event
                Toast.makeText(view.getContext(), "todo: start workout event", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete workout event
                removeAt(pos);
            }
        });

        return view;
    }

    public void removeAt(int position) {
        // Load account, workout from DB
        WorkoutEvent toRemove = this.eventList.get(position);
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        // Delete account ref, workout from DB
        account.removeEventId(view.getContext(), toRemove.workoutEventID);
        dbInstance.getWorkoutEventDao().deleteWorkoutEvent(toRemove);

        // Update adapter
        this.eventList.remove(position);
        notifyDataSetChanged();
    }

}
