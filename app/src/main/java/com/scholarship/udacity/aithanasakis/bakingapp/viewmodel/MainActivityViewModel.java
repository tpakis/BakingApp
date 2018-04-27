package com.scholarship.udacity.aithanasakis.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Resource;
import com.scholarship.udacity.aithanasakis.bakingapp.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class MainActivityViewModel extends ViewModel {

    @Inject
    RecipesRepository mRepository;
    //mediator lists because they transfer the changes of the livedata lists of the repository
    private MediatorLiveData<Resource<List<Recipe>>> recipesListObservable = new MediatorLiveData<Resource<List<Recipe>>>();
    @Inject
    public MainActivityViewModel() {
        super();
        BakingApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
        mRepository.fetchData();

        //subscribe to Livedata of the repository and pass it along to the view (activity - fragment etc)
        recipesListObservable.addSource(mRepository.getRecipesListObservable(), new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> recipes) {
                recipesListObservable.setValue(recipes);
            }
        });


    }

    public LiveData<Resource<List<Recipe>>> getRecipesListObservable() {
        return recipesListObservable;
    }
}
