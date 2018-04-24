package com.scholarship.udacity.aithanasakis.bakingapp.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

@Database(entities = {Recipe.class}, version = RecipesDB.VERSION)
public abstract class RecipesDB extends RoomDatabase {

    static final int VERSION = 1;
    public static final String DB_NAME = "app_db";

    public abstract RecipesDAO getRecipesDao();

}