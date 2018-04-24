package com.scholarship.udacity.aithanasakis.bakingapp.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Step;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class StepConverter {

    @TypeConverter
    public static List<Step> toList(String ingredientJson){
        if (ingredientJson == null) {
            return (null);
        }
        Type type = new TypeToken<List<Step>>() {}.getType();
        Gson gson = new Gson();
        List<Step> ingredientList = gson.fromJson(ingredientJson, type);
        return ingredientList;
    }
    @TypeConverter
    public static String toString(List<Step> ingredientList){
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();
        String json = gson.toJson(ingredientList, type);
        return json;
    }
}
