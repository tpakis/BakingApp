package com.scholarship.udacity.aithanasakis.bakingapp.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.app.Constants;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

import javax.annotation.Nullable;

import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 03/05/2018.
 */


public class RecipeDetailsActivity extends AppCompatActivity {
    private Recipe selectedRecipe;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        if (bundle != null && bundle.containsKey(Constants.SELECTEDRECIPE)) {
            selectedRecipe = bundle.getParcelable(Constants.SELECTEDRECIPE);
        }else if (savedInstanceState!=null) {
            selectedRecipe = savedInstanceState.getParcelable(Constants.RECIPEPARCEL);
        }else{
            Timber.d("no recipe to show error");
            finish();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.RECIPEPARCEL,selectedRecipe);
    }
    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }


}
