package com.example.fitnoise.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    public long profileId;
    public String firstName, lastName, gender;
    public int age, height, weight, chest, waist, hips;

    @Ignore
    public UserProfile(){
        this.profileId = 0;
        this.firstName = "";
        this.lastName = "";
        this.age = 0;
        this.gender = "";
        this.height = 0;
        this.weight = 0;
        this.chest = 0;
        this.waist = 0;
        this.hips = 0;
    }

    @Ignore
    public UserProfile(String first_name, String last_name, int age, String gender, int height, int weight, int chest, int waist, int hips){
        this.firstName = first_name;
        this.lastName = last_name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
    };

    public UserProfile(long profileId, String firstName, String lastName, int age, String gender, int height, int weight, int chest, int waist, int hips){
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
    };

    public String getFullName(){
        return String.format("%s %s", firstName, lastName);
    }


    public String getHeightCM(){
        return String.format("%scm", height);
    }

    public String getWeightCM(){
        return String.format("%scm", weight);
    }
    public String getChestCM(){
        return String.format("%scm", chest);
    }
    public String getWaistCM(){
        return String.format("%scm", waist);
    }
    public String getHipsCM(){
        return String.format("%scm", hips);
    }
}
