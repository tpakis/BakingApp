package com.scholarship.udacity.aithanasakis.bakingapp.repository;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.network.RecipeApi;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class RecipesRepository {
    private RecipesDAO recipesDAO;
    private RecipeApi recipeApi;
    @Inject
    public RecipesRepository(RecipesDAO recipesDAO, RecipeApi recipeApi){
        this.recipesDAO=recipesDAO;
        this.recipeApi=recipeApi;
    }

    public void getRecipesFromWeb(){
        recipeApi.getRecipesFromWeb().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                   // measurementsListObservable.setValue(response.body());
                    //   items.addAll(response.body().getResults());
                    //   setLiveData(items);
                    // addAllToDB(itemsData);
                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Timber.d("not found");
                            break;
                        case 500:
                            Timber.d("not logged in or server broken");
                            break;
                        default:
                            Timber.d("unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }
}
