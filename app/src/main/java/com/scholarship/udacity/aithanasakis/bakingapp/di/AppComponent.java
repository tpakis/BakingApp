package com.scholarship.udacity.aithanasakis.bakingapp.di;

import com.scholarship.udacity.aithanasakis.bakingapp.network.RecipeApi;
import com.scholarship.udacity.aithanasakis.bakingapp.viewmodel.MainActivityViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

@Singleton
@Component(modules = {AppModule.class,RetrofitModule.class,RecipesRepositoryModule.class,RoomDbModule.class})
public interface AppComponent {
    void inject(MainActivityViewModel viewModel);
    RecipeApi getRecipeApi();
}