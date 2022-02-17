package com.example.fitnoise.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public long exerciseID;
    public String name;
    public String description;
    public String imageURL;

    @Ignore
    public Exercise(){
        this.exerciseID = 0;
        this.name = "";
        this.description = "";
        this.imageURL = "";
    }
    @Ignore
    public Exercise(String name, String description, String imageURL) {
        this.exerciseID = 0;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    // Room Constructor
    public Exercise(long exerciseID, String name, String description, String imageURL) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    // The toString method is extremely important to making this class work with a Spinner
    // (or ListView) object because this is the method called when it is trying to represent
    // this object within the control.  If you do not have a toString() method, you WILL
    // get an exception.
    public String toString()
    {
        return name;
    }

}
