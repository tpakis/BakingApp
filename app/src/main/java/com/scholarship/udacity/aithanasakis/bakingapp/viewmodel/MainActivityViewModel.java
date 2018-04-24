package com.scholarship.udacity.aithanasakis.bakingapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.Handler;

import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;
import com.scholarship.udacity.aithanasakis.bakingapp.repository.RecipesRepository;

import javax.inject.Inject;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class MainActivityViewModel extends ViewModel {
    @Inject
    RecipesRepository mRepository;
    
    @Inject
    public MainActivityViewModel() {
        super();
        BakingApplication.getMyApplication().getMainActivityViewModelComponent().inject(this);
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                mRepository.getRecipesFromWeb();
            }
        };

        handler.postDelayed(r, 5000);
        //mRepository.getRecipesFromWeb();
    }
}
