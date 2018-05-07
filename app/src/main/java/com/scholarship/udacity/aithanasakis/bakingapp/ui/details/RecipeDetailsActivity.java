package com.scholarship.udacity.aithanasakis.bakingapp.ui.details;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.app.Constants;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.viewmodel.DetailsActivityViewModel;
import com.scholarship.udacity.aithanasakis.bakingapp.widget.RecipesWidgetProvider;
import com.scholarship.udacity.aithanasakis.bakingapp.widget.RecipesWidgetRemoteViewsService;


import javax.annotation.Nullable;

import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 03/05/2018.
 */


public class RecipeDetailsActivity extends AppCompatActivity {
    private Recipe selectedRecipe;
    private int selectedStepNumber;
    RecipeStepsFragment recipeStepsFragment;
    RecipeStepDetailsFragment recipeStepDetailsFragment;
    //check if device is in portrait or not, have different values in xml
    @BindBool(R.bool.portrait)
    public boolean isDeviceInPortrait;
    @BindInt(R.integer.grid_columns)
    public int columnsCount;
    @BindView(R.id.steps_fragment_container)
    FrameLayout stepsFragmentContainer;
    @Nullable
    @BindView(R.id.step_details_fragment_container)
    FrameLayout stepDetailsFragmentContainer;
    FragmentManager fragmentManager;
    private DetailsActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);
        if (bundle != null && bundle.containsKey(Constants.SELECTEDRECIPE)) {
            selectedRecipe = bundle.getParcelable(Constants.SELECTEDRECIPE);
            viewModel.setSelectedRecipe(selectedRecipe);
        }else {
            selectedRecipe = viewModel.getSelectedRecipe();
            selectedStepNumber = viewModel.getSelectedStepNumber();
        }
        if (selectedRecipe == null){
            Timber.d("no recipe to show error");
            finish();
        }
        fragmentManager = getSupportFragmentManager();
        //Check if fragment exists to restore it after rotation
        Fragment fragment = fragmentManager.findFragmentByTag(Constants.RECIPESTEPSFRAGMENTTAG);
        if(fragment == null) {
            recipeStepsFragment = new RecipeStepsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.steps_fragment_container, recipeStepsFragment,Constants.RECIPESTEPSFRAGMENTTAG)
                    .commit();
        }else{
            recipeStepsFragment = (RecipeStepsFragment)fragment;
        }
        Fragment fragment2 = fragmentManager.findFragmentByTag(Constants.STEPDETAILSFRAGMENTTAG);
        if(fragment2 == null) {
            recipeStepDetailsFragment = new RecipeStepDetailsFragment();
            if (stepDetailsFragmentContainer!=null){
                fragmentManager.beginTransaction()
                        .add(R.id.step_details_fragment_container, recipeStepDetailsFragment,Constants.STEPDETAILSFRAGMENTTAG)
                        .commit();
            }
        }else{
            recipeStepDetailsFragment = (RecipeStepDetailsFragment)fragment2;
        }
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            switch (id) {
                case R.id.add_to_widget:
                    viewModel.addToWidget();
                    Snackbar.make(stepsFragmentContainer,getString(R.string.added_to_widget),Snackbar.LENGTH_LONG).show();
                    RecipesWidgetRemoteViewsService.updateWidget(this,selectedRecipe);
                    break;
            }
        return super.onOptionsItemSelected(item);
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    public int getSelectedStepNumber() {
        return selectedStepNumber;
    }

    public void setSelectedStepNumber(int selectedStepNumber) {
        this.selectedStepNumber = selectedStepNumber;
        viewModel.setSelectedStepNumber(selectedStepNumber);
        if (stepDetailsFragmentContainer==null&&!getResources().getBoolean(R.bool.portrait)){
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_fragment_container, recipeStepDetailsFragment,Constants.STEPDETAILSFRAGMENTTAG)
                    .commit();
        }
    }
}
