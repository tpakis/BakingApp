package com.scholarship.udacity.aithanasakis.bakingapp.viewmodel;


import android.arch.lifecycle.ViewModel;

import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.repository.RecipesRepository;

import javax.inject.Inject;

/**
 * Created by 3piCerberus on 07/05/2018.
 */

public class DetailsActivityViewModel extends ViewModel {
    private Recipe selectedRecipe;
    private int selectedStepNumber =0;
    @Inject
    RecipesRepository mRepository;

    @Inject
    public DetailsActivityViewModel() {
        super();
        BakingApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }

    public int getSelectedStepNumber() {
        return selectedStepNumber;
    }

    public void setSelectedStepNumber(int selectedStepNumber) {
        this.selectedStepNumber = selectedStepNumber;
    }

    //sets the selected recipe for new widget recipe
    public void addToWidget(){
        selectedRecipe.setForWidget(1);
        mRepository.setRecipeForWidget(selectedRecipe);
    }

}