package com.example.fitnoise.ui.exercise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitnoise.R;
import com.example.fitnoise.data.DatabaseFitnoise;
import com.example.fitnoise.data.Exercise;
import com.example.fitnoise.data.UserAccount;
import com.example.fitnoise.data.UserSession;
import com.example.fitnoise.data.Workout;

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
import android.widget.TextView;
import android.widget.Toast;

public class DisplayExercise extends AppCompatActivity {
    EditText name, desc;
    ImageView image;
    Button delete, updateBtn;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Uri uri = null;


    DatabaseFitnoise dbInstance;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_exercise);

        // DB Context
        dbInstance = DatabaseFitnoise.getDatabase(this);
        session = getIntent().getParcelableExtra("session");
        long exerciseId = getIntent().getLongExtra("exerciseId", 0);

        // Widgets
        name = (EditText)findViewById(R.id.name);
        desc = (EditText)findViewById(R.id.desc);
        image = (ImageView)findViewById(R.id.imgView2);
        delete = (Button)findViewById(R.id.delete);
        updateBtn = (Button)findViewById(R.id.updateBtn);

        // Load exercise from DB
        Exercise exercise = dbInstance.getExerciseDao().getExerciseById(exerciseId);

        String n = exercise.name;
        String d = exercise.description;
        String i = exercise.imageURL;

        if(!i.equals("")) {
            uri = Uri.parse(i);
        }

        name.setText(n);
        desc.setText(d);
        if (uri != null){
            image.setImageURI(uri);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete exercise from database
                dbInstance.getExerciseDao().deleteExercise(exercise);

                // Remove account ref
                UserAccount account = dbInstance.getUserAccountDao().getUserAccountById(session.userId);
                account.removeExerciseId(getApplicationContext(), exercise.exerciseID);
                dbInstance.getUserAccountDao().updateUserAccount(account);

                // Remove workout ref
                Workout[] allWorkouts = account.getAllWorkouts(getApplicationContext());
                for (Workout w: allWorkouts){
                    if (w.exerciseId == exercise.exerciseID){
                        w.exerciseId = 0;
                    }
                }
                dbInstance.getWorkoutDao().updateAllWorkouts(allWorkouts);

                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Form validation
                if(n.isEmpty() || d.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String nName = name.getText().toString();
                    String nDesc = desc.getText().toString();
                    String nImageUrl = "";
                    if (uri != null){
                        nImageUrl = uri.toString();
                    }

                    // UPDATE exercise
                    exercise.name = nName;
                    exercise.description = nDesc;
                    exercise.imageURL = nImageUrl;
                    dbInstance.getExerciseDao().updateExercise(exercise);
                    finish();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
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
            image.setImageURI(data.getData());
            uri = data.getData();
        }
    }


}