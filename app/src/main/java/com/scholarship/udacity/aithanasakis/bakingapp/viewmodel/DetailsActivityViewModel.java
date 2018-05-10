package com.scholarship.udacity.aithanasakis.bakingapp.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.repository.RecipesRepository;

import javax.inject.Inject;

/**
 * Created by 3piCerberus on 07/05/2018.
 */

public class DetailsActivityViewModel extends ViewModel {
    @Inject
    RecipesRepository mRepository;
    private Recipe selectedRecipe;
    private long playerPosition =0;
    private boolean playWhenReady = true;
    private boolean fragmentStepDetailsVisible = false;
    private MediatorLiveData<Integer> selectedStepObservable = new MediatorLiveData<Integer>();

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

    public LiveData<Integer> getSelectedStepNumber() {
        return selectedStepObservable;
    }

    public void setSelectedStepNumber(int selectedStepNumber) {
        resetPlayerState();
        selectedStepObservable.setValue(selectedStepNumber);
    }
    public void resetPlayerState(){
        playerPosition =0;
        playWhenReady = true;
    }
    //sets the selected recipe for new widget recipe
    public void addToWidget(){
        selectedRecipe.setForWidget(1);
        mRepository.setRecipeForWidget(selectedRecipe);
    }

    public long getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(long playerPosition) {
        this.playerPosition = playerPosition;
    }

    public boolean getPlayWhenReady() {
        return playWhenReady;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }


    public boolean isFragmentStepDetailsVisible() {
        return fragmentStepDetailsVisible;
    }

    public void setFragmentStepDetailsVisible(boolean fragmentStepDetailsVisible) {
        this.fragmentStepDetailsVisible = fragmentStepDetailsVisible;
    }
}