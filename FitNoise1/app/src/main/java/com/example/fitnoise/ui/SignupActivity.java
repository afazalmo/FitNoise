package com.example.fitnoise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DBClient;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserProfile;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    DatabaseFitnoise dbInstance;

    EditText editUsername, editPassword, editConfirmPassword, editEmail;
    Button btnRegister, btnView, btnBack;
    CheckBox checkboxAgreement;

    ArrayList<String> usernameList;
    ArrayList<String> passwordList;
    ArrayList<String> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbInstance = DBClient.getDatabase(this);

        // Link widgets
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        //btnView = (Button) findViewById(R.id.btnView);
        btnBack = (Button) findViewById(R.id.btnReturn);
        checkboxAgreement = (CheckBox) findViewById(R.id.checkboxAgreement);

        usernameList = new ArrayList<String>();
        passwordList = new ArrayList<String>();
        emailList = new ArrayList<String>();

        // Listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String email = editEmail.getText().toString();

                // Validate data
                if (isFormValid()){
                    // Create empty profile
                    long profileId = dbInstance.getUserProfileDao().insertUserProfile(new UserProfile());

                    // Create new account
                    UserAccount account = new UserAccount(username, password, email);
                    account.setProfileId(profileId);
                    dbInstance.getUserAccountDao().insertUserAccount(account);

                    Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_SHORT).show();

                    //Return to login screen
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid form submission!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean isFormValid(){
        // terms not accepted
        if (!checkboxAgreement.isChecked()){
            return false;
        }
        // empty username
        else if (editUsername.getText().toString().isEmpty()) {
            return false;
        }
        // empty password
        else if (editPassword.getText().toString().isEmpty()) {
            return false;
        }
        // empty confirm password
        else if (editConfirmPassword.getText().toString().isEmpty()) {
            return false;
        }
        // empty email
        else if (editEmail.getText().toString().isEmpty()) {
            return false;
        }
        // password does not match confirm password
        else if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
            return false;
        }

        return true;
    }

    // EULA info
    public void onClick (View view){
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        startActivity(intent);
    }


}