package com.example.fitnoise.ui.exercise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;

public class AddExerciseActivity extends AppCompatActivity {
    EditText exName,exDesc;
    Button addEx;
    ImageView mImageView;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Uri uri = null;

    // DB Context
    DatabaseFitnoise dbInstance;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        // Get DB Context
        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");

        exName = (EditText) findViewById(R.id.name);
        exDesc = (EditText) findViewById(R.id.desc);
        addEx = (Button) findViewById(R.id.add);
        mImageView = (ImageView)findViewById(R.id.imgView);

        addEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = exName.getText().toString();
                String desc = exDesc.getText().toString();
                String u = "";
                if (uri != null){
                    u = uri.toString();
                }

                // Form validation
                if(name.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Create exercise
                    Exercise newExercise = new Exercise(name, desc, u);
                    long exerciseId = dbInstance.getExerciseDao().insertExercise(newExercise);

                    // Update account with exerciseId ref
                    UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
                    account.addExerciseId(getApplicationContext(), exerciseId);
                    dbInstance.getUserAccountDao().updateUserAccount(account);

                    //Intent intent = new Intent(AddExercise.this, GalleryFragment.class);
                    //startActivity(intent);
                    finish();
                }
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }

            }
        });

    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(this,"Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImageView.setImageURI(data.getData());
            uri = data.getData();
        }
    }

}