package com.scholarship.udacity.aithanasakis.bakingapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Resource;
import com.scholarship.udacity.aithanasakis.bakingapp.viewmodel.MainActivityViewModel;

import java.util.List;



import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.v("Activity Started!");
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getRecipesListObservable().observe(MainActivity.this, new Observer<Resource<List<Recipe>>>() {
           @Override
           public void onChanged(@Nullable Resource<List<Recipe>> recipes) {
              Timber.d(recipes.status.toString());
           }
       });
    }
}
