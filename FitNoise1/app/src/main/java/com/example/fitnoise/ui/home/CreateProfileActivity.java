package com.example.fitnoise.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserProfile;
import com.example.fitnoise.data.UserSession;

public class CreateProfileActivity extends AppCompatActivity {
    EditText firstName, lastName, age, height, weight, chest, waist, hips;
    Spinner gender;
    Button saveProfileBtn;
    UserSession session;
    DatabaseFitnoise dbInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");

        // Widgets
        firstName = (EditText) findViewById(R.id.fname);
        lastName = (EditText) findViewById(R.id.lname);
        age = (EditText) findViewById(R.id.age);
        gender = (Spinner) findViewById(R.id.genderSpinner);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        chest = (EditText) findViewById(R.id.chest);
        waist = (EditText) findViewById(R.id.waist);
        hips = (EditText) findViewById(R.id.hips);
        saveProfileBtn = (Button) findViewById(R.id.saveProfileBtn);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spin
        gender.setAdapter(adapter);


        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myfName = firstName.getText().toString();
                String mylName = lastName.getText().toString();
                String myAge = age.getText().toString();
                String myGender = gender.getSelectedItem().toString();;
                String myHeight = height.getText().toString();
                String myWeight = weight.getText().toString();
                String myChest = chest.getText().toString();
                String myWaist = waist.getText().toString();
                String myHips = hips.getText().toString();

                // Form validation
                if (myfName.isEmpty() || mylName.isEmpty() || myAge.isEmpty() || myGender.isEmpty() ||
                        myHeight.isEmpty() || myWeight.isEmpty() || myChest.isEmpty() || myWaist.isEmpty() || myHips.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Missing Input Fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Load profile from account
                    UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
                    UserProfile myProfile = account.getProfile(getApplicationContext());

                    // Update profile
                    myProfile.firstName = myfName;
                    myProfile.lastName = mylName;
                    myProfile.age = Integer.parseInt(myAge);
                    myProfile.gender = myGender;
                    myProfile.height = Integer.parseInt(myHeight);
                    myProfile.weight = Integer.parseInt(myWeight);
                    myProfile.chest = Integer.parseInt(myChest);
                    myProfile.waist = Integer.parseInt(myWaist);
                    myProfile.hips = Integer.parseInt(myHips);

                    // Save to DB
                    dbInstance.getUserProfileDao().updateUserProfile(myProfile);
                    Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });

    }
}