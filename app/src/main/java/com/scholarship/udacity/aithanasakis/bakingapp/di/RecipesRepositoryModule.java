package com.scholarship.udacity.aithanasakis.bakingapp.di;

import com.scholarship.udacity.aithanasakis.bakingapp.repository.RecipesRepository;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

@Module(includes = RoomDbModule.class)
public class RecipesRepositoryModule {

    @Provides
    @Singleton
    public RecipesRepository getRecipesRepository(RecipesDAO recipesDAO){
        return new RecipesRepository(recipesDAO);
    }
}
