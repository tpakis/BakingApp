package com.scholarship.udacity.aithanasakis.bakingapp.di;

import android.arch.persistence.room.Room;

import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 3piCerberus on 24/04/2018.
 */
@Module(includes = AppModule.class)
public class RoomDbModule {
    private RecipesDB recipesDB;

    public RoomDbModule(BakingApplication mApplication) {
        recipesDB = Room.databaseBuilder(mApplication, RecipesDB.class, RecipesDB.DB_NAME).build();
    }

    @Singleton
    @Provides
    RecipesDB providesRoomDatabase() {
        return recipesDB;
    }

    @Singleton
    @Provides
    RecipesDAO providesProductDao(RecipesDB recipesDB) {
        return recipesDB.getRecipesDao();
    }
}
