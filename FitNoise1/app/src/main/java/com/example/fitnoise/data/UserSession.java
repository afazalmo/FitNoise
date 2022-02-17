package com.example.fitnoise.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UserSession implements Parcelable {

    // Session data to be used in all the views
    public long userId;
    public String username;


    public UserSession() {
        this.userId = -1;
        this.username = "Guest";
    }

    public UserSession(long _id, String username) {
        this.userId = _id;
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    // Parcelable stuff below
    protected UserSession(Parcel in) {
        userId = in.readLong();
        username = in.readString();
    }

    public static final Creator<UserSession> CREATOR = new Creator<UserSession>() {
        @Override
        public UserSession createFromParcel(Parcel in) {
            return new UserSession(in);
        }

        @Override
        public UserSession[] newArray(int size) {
            return new UserSession[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(userId);
        parcel.writeString(username);
    }


}
