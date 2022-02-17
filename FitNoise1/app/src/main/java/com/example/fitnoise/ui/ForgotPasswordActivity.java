package com.example.fitnoise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;


public class ForgotPasswordActivity extends AppCompatActivity {
    public static final String COL3="email";
    private EditText emailText;
    private EditText usernameText;
    private EditText newPasswordText;
    private Button resetPasswordBtn;
    private Button cancelBtn;

    // DB Context
    DatabaseFitnoise dbInstance;
    // no session - not logged in yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(this);

        emailText = (EditText) findViewById(R.id.email);
        usernameText = (EditText) findViewById(R.id.username);
        newPasswordText = (EditText) findViewById(R.id.new_password);
        resetPasswordBtn = (Button) findViewById(R.id.reset_password);
        cancelBtn = (Button) findViewById(R.id.cancelButton);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String user = usernameText.getText().toString();
                String newPass = newPasswordText.getText().toString();

                boolean found = false;

                // Find matching email and username in DB
                UserAccount[] allAccounts = dbInstance.getUserAccountDao().getAllUserAccounts();
                UserAccount targetAccount = null;
                for (int x = 0; x < allAccounts.length; x++) {
                    if (allAccounts[x].email.equals(email) && allAccounts[x].username.equals(user)) {
                        // Load their account from DB
                        targetAccount = allAccounts[x];
                        found = true;
                        break;
                    }
                }

                if(found && targetAccount != null) {
                    // Save new password to DB
                    targetAccount.password = newPass;
                    dbInstance.getUserAccountDao().updateUserAccount(targetAccount);

                    // Perform login
                    UserSession session = new UserSession(targetAccount.accountID, targetAccount.username);
                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    intent.putExtra("session", session);

                    finish();
                    startActivity(intent);
                    Toast.makeText(ForgotPasswordActivity.this, "Password changed!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}