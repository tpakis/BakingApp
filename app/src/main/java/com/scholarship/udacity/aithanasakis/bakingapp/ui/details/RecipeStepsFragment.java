package com.scholarship.udacity.aithanasakis.bakingapp.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.adapter.RecipeStepsAdapter;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Ingredient;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Step;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 03/05/2018.
 */

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler {
    @BindView(R.id.ingredients_text)
    TextView ingredientsText;
    @BindView(R.id.recycler_view_recipe_steps_list)
    RecyclerView recyclerViewRecipeStepsList;
    Unbinder unbinder;
    private RecipeDetailsActivity parent;
    private Recipe selectedRecipe;
    private LinearLayoutManager mLinearLayoutManager;
    private RecipeStepsAdapter mRecipeStepsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (RecipeDetailsActivity) this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewgroup = inflater.inflate(R.layout.steps_fragment, container, false);
        unbinder = ButterKnife.bind(this, viewgroup);
        selectedRecipe = parent.getSelectedRecipe();
        setupIngredients();
        //setup recyclerview
        mLinearLayoutManager = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
        recyclerViewRecipeStepsList.setLayoutManager(mLinearLayoutManager);
        mRecipeStepsAdapter = new RecipeStepsAdapter(this);
        recyclerViewRecipeStepsList.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsAdapter.setStepsListToShow(selectedRecipe.getSteps());
        return viewgroup;
    }

    // recycler adapter on click
    @Override
    public void onClick(Step selectedStepItem) {
        parent.setSelectedStepNumber(selectedStepItem.getId());
        Timber.d(selectedStepItem.getDescription());
    }

    private void setupIngredients() {
        StringBuilder ingredientsSb = new StringBuilder();
        for (Ingredient ingredient : selectedRecipe.getIngredients()) {
            ingredientsSb.append(String.format(Locale.getDefault(), "• %s (%d %s)", ingredient.getIngredient(), ingredient.getQuantity().intValue(), ingredient.getMeasure()));
            ingredientsSb.append("\n");
        }
        ingredientsText.setText(ingredientsSb.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
