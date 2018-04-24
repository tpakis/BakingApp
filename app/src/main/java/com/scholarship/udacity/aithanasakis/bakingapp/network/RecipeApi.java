package com.scholarship.udacity.aithanasakis.bakingapp.network;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by 3piCerberus on 24/04/2018.
 */

public interface RecipeApi {
    @GET("2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipesFromWeb();
}
