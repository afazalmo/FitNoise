package com.example.fitnoise.ui.workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.ui.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutsFragment extends Fragment {
    private ArrayList<Workout> lstWorkout;
    WorkoutAdapter workoutAdapter;
    ListView listView;

    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        listView = (ListView) view.findViewById(R.id.workoutList);
        Button add = (Button) view.findViewById(R.id.add_button);

          // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(getContext());
        session = ((MainActivity)getActivity()).getSession();

        lstWorkout = new ArrayList<>();


        // Load Workouts
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        Workout[] workouts = account.getAllWorkouts(getContext());
        // Convert from Workout[] -> List<Workout> -> ArrayList<Workout>
        lstWorkout = new ArrayList<Workout>(Arrays.asList(workouts));

        workoutAdapter = new WorkoutAdapter(getActivity(), lstWorkout, session);
        listView.setAdapter(workoutAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddWorkoutActivity.class);
                intent.putExtra("session", session); // must pass session when switching activity
                startActivity(intent);
            }
        });

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getContext(), DisplayWorkout.class);
                in.putExtra("wId", lstWorkout.get(i).workoutID);
                //in.putExtra("eId", lstWorkout.get(i).exerciseId);
                in.putExtra("Name", lstWorkout.get(i).getName());
                in.putExtra("Exercise", lstWorkout.get(i).getExercise());
                in.putExtra("Description", lstWorkout.get(i).getDescription());
                in.putExtra("Sets", lstWorkout.get(i).getSets());
                in.putExtra("Reps", lstWorkout.get(i).getReps());
                startActivity(in);
            }
        });*/

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        lstWorkout.clear();

        // Load Workouts
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        Workout[] workouts = account.getAllWorkouts(getContext());
        // Convert from Workout[] -> List<Workout> -> ArrayList<Workout>
        lstWorkout = new ArrayList<Workout>(Arrays.asList(workouts));

        workoutAdapter = new WorkoutAdapter(getActivity(), lstWorkout, session);
        listView.setAdapter(workoutAdapter);
    }


}