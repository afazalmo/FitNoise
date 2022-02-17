package com.example.fitnoise.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.ui.MainActivity;

import java.util.ArrayList;

public class ExerciseFragment extends Fragment {
    private TeamAdapter adapter;
    //ArrayList<String> descL;
    //ArrayList<String> nameL;
    //ArrayList<String> imageL;
    ListView list;

    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(getContext());
        session = ((MainActivity)getActivity()).getSession();

        // Widgets
        Button add = root.findViewById(R.id.addEx);
        list = (ListView) root.findViewById(R.id.list);

        //descL = new ArrayList<String>();
        //nameL = new ArrayList<String>();
        //imageL = new ArrayList<String>();

        // Load Exercises
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        Exercise[] exercises = account.getAllExercises(getContext());

        // Update adapter
        adapter = new TeamAdapter(getActivity(), exercises);
        list.setAdapter(adapter);


        // Add Exercise
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddExerciseActivity.class);
                intent.putExtra("session", session); // must pass session when switching activity
                startActivity(intent);
            }
        });

        // Exercise Item Click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Exercise selected = (Exercise)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), DisplayExercise.class);
                intent.putExtra("exerciseId", selected.exerciseID);
                intent.putExtra("session", session);
                /*
                intent.putExtra("nameList", nameL.get(i));
                intent.putExtra("descList", descL.get(i));
                intent.putExtra("imageList", imageL.get(i));*/
                startActivity(intent);
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Load Exercises
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        Exercise[] exercises = account.getAllExercises(getContext());

        // Update adapter
        adapter = new TeamAdapter(getActivity(), exercises);
        list.setAdapter(adapter);
    }
}
