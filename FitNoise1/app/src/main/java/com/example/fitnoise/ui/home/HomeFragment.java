package com.example.fitnoise.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.ui.MainActivity;
import com.example.fitnoise.R;
import com.example.fitnoise.data.UserProfile;
import com.example.fitnoise.data.UserSession;

public class HomeFragment extends Fragment {
    DatabaseFitnoise dbInstance;
    Button createProfileBtn;
    UserSession session;
    TextView myName, myAge, myGender, myHeight, myWeight, myChest, myWaist, myHips;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        myName  = (TextView)root.findViewById(R.id.homeFirstLast);
        myAge  = (TextView)root.findViewById(R.id.homeAge);
        myGender  = (TextView)root.findViewById(R.id.homeGender);
        myHeight  = (TextView)root.findViewById(R.id.homeHeight);
        myWeight  = (TextView)root.findViewById(R.id.homeWeight);
        myChest  = (TextView)root.findViewById(R.id.homeChest);
        myWaist  = (TextView)root.findViewById(R.id.homeWaist);
        myHips  = (TextView)root.findViewById(R.id.homeHips);
        createProfileBtn = (Button)root.findViewById(R.id.createProfileBtn);

        dbInstance = DatabaseFitnoise.getDatabase(getContext());
        // Get session
        session = ((MainActivity)getActivity()).getSession();

        // Load profile
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        UserProfile myProfile = account.getProfile(getContext());

        // Set profile
        myName.setText(myProfile.getFullName());
        myAge.setText(String.valueOf(myProfile.age));
        myGender.setText(myProfile.gender);
        myHeight.setText(myProfile.getHeightCM());
        myWeight.setText(myProfile.getWeightCM());
        myChest.setText(myProfile.getChestCM());
        myWaist.setText(myProfile.getWaistCM());
        myHips.setText(myProfile.getHipsCM());

        // Edit profile
        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProfileIntent = new Intent(getContext(), CreateProfileActivity.class);
                newProfileIntent.putExtra("session", session);
                startActivity(newProfileIntent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load profile
        UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
        UserProfile myProfile = account.getProfile(getContext());

        // Set profile
        myName.setText(myProfile.getFullName());
        myAge.setText(String.valueOf(myProfile.age));
        myGender.setText(myProfile.gender);
        myHeight.setText(myProfile.getHeightCM());
        myWeight.setText(myProfile.getWeightCM());
        myChest.setText(myProfile.getChestCM());
        myWaist.setText(myProfile.getWaistCM());
        myHips.setText(myProfile.getHipsCM());

    }
}