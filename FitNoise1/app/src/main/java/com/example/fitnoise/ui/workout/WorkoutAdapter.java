package com.example.fitnoise.ui.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.data.WorkoutEvent;

import java.util.ArrayList;

public class WorkoutAdapter extends BaseAdapter {
    View view;
    Context context;
    ArrayList<Workout> workoutList;
    LayoutInflater inflater;
    UserSession session;
    DatabaseFitnoise dbInstance;

    public WorkoutAdapter (Context appContext, ArrayList<Workout> workoutList, UserSession session){
        this.context = appContext;
        this.workoutList = workoutList;
        inflater = LayoutInflater.from(appContext);
        this.session = session;
    }

    @Override
    public int getCount() {
        return workoutList.size();
    }

    @Override
    public Object getItem(int i) {
        return workoutList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int pos, View someView, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.workout_item, null, true);
        //TextView num = (TextView) view.findViewById(R.id.wID);
        TextView name = (TextView) view.findViewById(R.id.wName);
        TextView exercise_name = (TextView) view.findViewById(R.id.wExercise);
        TextView description_ = (TextView) view.findViewById(R.id.wDescription);
        TextView sets = (TextView) view.findViewById(R.id.wSets);
        TextView reps = (TextView) view.findViewById(R.id.wReps);
        Button delete = (Button) view.findViewById(R.id.delete_button);
        CardView workoutLayout = (CardView)view.findViewById(R.id.workout_item_id);

        dbInstance = DatabaseFitnoise.getDatabase(view.getContext());

        // Load objects
        Workout myWorkout = workoutList.get(pos);
        Exercise myExercise = workoutList.get(pos).getExercise(view.getContext());

        // Update display
        //String idString = "Id: " + myWorkout.workoutID;
        String setsString = "Sets: " + myWorkout.sets;;
        String repsString = "Reps: " + myWorkout.reps;;

        //num.setText(idString);
        name.setText(myWorkout.name);
        exercise_name.setText(myExercise.name);
        description_.setText(myWorkout.description);
        sets.setText(setsString);
        reps.setText(repsString);

        // List item
        workoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edit workout item
                //Toast.makeText(view.getContext(), "Clicked on " + workoutList.get(pos).name, Toast.LENGTH_SHORT).show();
                Intent updateIntent = new Intent(view.getContext(), DisplayWorkoutActivity.class);
                updateIntent.putExtra("session", session);
                updateIntent.putExtra("workoutId", workoutList.get(pos).workoutID);
                view.getContext().startActivity(updateIntent);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeAt(pos);
            }
        });


        return view;
    }

    public void removeAt(int position) {
        // Delete workout from DB
        Workout toRemove = this.workoutList.get(position);
        dbInstance.getWorkoutDao().deleteWorkout(toRemove);

        // remove account workout ref
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        account.removeWorkoutId(view.getContext(), toRemove.workoutID);

        // Remove workout event ref
        WorkoutEvent[] allWorkoutEvents = account.getAllEvents(view.getContext());
        for (WorkoutEvent wEvent: allWorkoutEvents){
            if (wEvent.refWorkoutId == toRemove.workoutID){
                wEvent.refWorkoutId = 0;
            }
        }
        dbInstance.getWorkoutEventDao().updateAllWorkoutEvents(allWorkoutEvents);

        // Update adapter
        this.workoutList.remove(position);
        notifyDataSetChanged();
    }



}
