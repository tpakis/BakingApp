package com.scholarship.udacity.aithanasakis.bakingapp.repository;

import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import javax.inject.Inject;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class RecipesRepository {
    private RecipesDAO recipesDAO;
    @Inject
    public RecipesRepository(RecipesDAO recipesDAO){
        this.recipesDAO=recipesDAO;
    }
}
