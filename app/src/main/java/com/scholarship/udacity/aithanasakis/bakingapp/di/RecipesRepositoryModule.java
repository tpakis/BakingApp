package com.scholarship.udacity.aithanasakis.bakingapp.di;

import com.scholarship.udacity.aithanasakis.bakingapp.network.RecipeApi;
import com.scholarship.udacity.aithanasakis.bakingapp.repository.RecipesRepository;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

@Module(includes = {RoomDbModule.class,RetrofitModule.class})
public class RecipesRepositoryModule {

    @Provides
    @Singleton
    public RecipesRepository getRecipesRepository(RecipesDAO recipesDAO, RecipeApi recipeApi){
        return new RecipesRepository(recipesDAO,recipeApi);
    }
}
