package com.example.fitnoise.ui.exercise;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnoise.R;
import com.example.fitnoise.data.Exercise;

import java.util.ArrayList;


class TeamAdapter extends BaseAdapter {
    private Activity mContext;
    //int mResource;
    private Exercise[] exerciseArray;
    //private ArrayList<String> desc;
    //private ArrayList<String> name;

    private static final String TAG = "TeamAdapter";
    public TeamAdapter(Activity context, Exercise[] exerciseArray){
        mContext = context;
        this.exerciseArray = exerciseArray;
    }


    @Override
    public int getCount() {
        return exerciseArray.length;
    }

    @Override
    public Object getItem(int i) {
        return exerciseArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.individual_exercise, null, true);

        TextView tvName = (TextView)rowView.findViewById(R.id.name);

        String eName = exerciseArray[position].name;
        tvName.setText(eName);

        return rowView;
    }
}