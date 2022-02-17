package com.example.fitnoise.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;
import com.example.fitnoise.data.WorkoutEvent;
import com.example.fitnoise.ui.MainActivity;
import com.example.fitnoise.ui.workout.WorkoutAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutEventFragment extends Fragment {
    private ArrayList<WorkoutEvent> workoutEventArray;

    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(getContext());
        session = ((MainActivity)getActivity()).getSession();

        Button addEventBtn = (Button)root.findViewById(R.id.add_event);
        listView = (ListView)root.findViewById(R.id.workoutEventList);

        // Load Workout Events
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        WorkoutEvent[] workoutEvents = account.getAllEvents(getContext());
        workoutEventArray = new ArrayList<WorkoutEvent>(Arrays.asList(workoutEvents));
        EventAdapter eventAdapter = new EventAdapter(getContext(), workoutEventArray, session);
        listView.setAdapter(eventAdapter);

        //ArrayAdapter<WorkoutEvent> eventAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, workoutEventArray);


        // Listeners
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateWorkoutEventActivity.class);
                intent.putExtra("session", ((MainActivity)getActivity()).getSession());
                startActivity(intent);
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        workoutEventArray.clear();

        // Load Workout Events
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        WorkoutEvent[] workoutEvents = account.getAllEvents(getContext());
        workoutEventArray = new ArrayList<WorkoutEvent>(Arrays.asList(workoutEvents));
        EventAdapter eventAdapter = new EventAdapter(getContext(), workoutEventArray, session);
        listView.setAdapter(eventAdapter);
    }


}



