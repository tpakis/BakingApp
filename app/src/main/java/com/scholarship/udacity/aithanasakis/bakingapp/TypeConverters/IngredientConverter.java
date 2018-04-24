package com.scholarship.udacity.aithanasakis.bakingapp.TypeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Ingredient;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class IngredientConverter {

   // Gson gson = new Gson();
   // String json = gson.toJson(user);
    @TypeConverter
    public static List<Ingredient> toList(String ingredientJson){
        if (ingredientJson == null) {
            return (null);
        }
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        Gson gson = new Gson();
        List<Ingredient> ingredientList = gson.fromJson(ingredientJson, type);
        return ingredientList;
    }
    @TypeConverter
    public static String toString(List<Ingredient> ingredientList){
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        String json = gson.toJson(ingredientList, type);
        return json;
    }
}
