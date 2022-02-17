package com.example.fitnoise.data;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Converters {

    // Long -> Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    // Date -> Long
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    // String: json -> long[]
    @TypeConverter
    public static long[] stringToIntArray(String value) {
        ArrayList<Long> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(value);
            if (jsonArray.length() == 0){
                long[] a = null;
                return a;
            }
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                long myInt = jsonObj.getLong("i");
                arrayList.add(myInt);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        long[] arrayInts = new long[arrayList.size()];
        for (int x = 0; x < arrayList.size(); x++){
            arrayInts[x] = arrayList.get(x);
        }

        return arrayInts;
    }

    // String: long[] -> json
    @TypeConverter
    public static String intArrayToString(long[] longArray) {
        if (longArray == null) {return "[]";}
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 0; i < longArray.length; i++){
            if (i == 0){
                sb.append("{i: ");
                sb.append(longArray[0]);
                sb.append("}");
            }
            else {
                sb.append(",{i: ");
                sb.append(longArray[i]);
                sb.append("}");
            }
        }
        sb.append("]");

        String json = sb.toString();
        return json;
    }

    /*
    // String: json -> ArrayList<Long>
    @TypeConverter
    public static ArrayList<Long> stringToArrayList(String value) {
        ArrayList<Long> arrayInts = new ArrayList<Long>();
        try {
            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                long myInt = jsonObj.getLong("intVal");
                arrayInts.add(myInt);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return arrayInts;
    }

    // ArrayList<Long> -> String: json
    @TypeConverter
    public static String arrayListToString(ArrayList<Long> arrayList) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 0; i < arrayList.size(); i++){
            if (i == 0){
                sb.append("{intVal: ");
                sb.append(arrayList.get(0));
                sb.append("}");
            }
            else {
                sb.append(",{intVal: ");
                sb.append(arrayList.get(i));
                sb.append("}");
            }
        }
        sb.append("]");

        String json = sb.toString();
        return json;
    }
    */
}
