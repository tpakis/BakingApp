package com.scholarship.udacity.aithanasakis.bakingapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.adapter.RecipesMainAdapter;
import com.scholarship.udacity.aithanasakis.bakingapp.app.Constants;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Resource;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Status;
import com.scholarship.udacity.aithanasakis.bakingapp.ui.details.RecipeDetailsActivity;
import com.scholarship.udacity.aithanasakis.bakingapp.viewmodel.MainActivityViewModel;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesMainAdapter.RecipesMainAdapterOnClickHandler, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view_recipes_list)
    RecyclerView recyclerViewRecipesList;
    @BindView(R.id.todo_list_empty_view)
    LinearLayout todoListEmptyView;
    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout pullToRefresh;
    private MainActivityViewModel viewModel;
    private GridLayoutManager mGridLayoutManager;
    private RecipesMainAdapter mRecipesMainAdapter;
    @BindInt(R.integer.grid_columns)
    public int columnsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.v("Activity Started!");
        //setup RecyclerView
        mGridLayoutManager = new GridLayoutManager(this, columnsCount);
        recyclerViewRecipesList.setLayoutManager(mGridLayoutManager);
        mRecipesMainAdapter = new RecipesMainAdapter(this, MainActivity.this);
        recyclerViewRecipesList.setAdapter(mRecipesMainAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewRecipesList.getContext(),
                mGridLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider));
        pullToRefresh.setOnRefreshListener(this);
        recyclerViewRecipesList.addItemDecoration(dividerItemDecoration);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        pullToRefresh.setRefreshing(true);
        viewModel.getRecipesListObservable().observe(MainActivity.this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> recipes) {
                Timber.d("ttttttttttt" + recipes.status.toString());
                if (recipes.status!= Status.LOADING){
                    pullToRefresh.setRefreshing(false);
                }
                if (recipes.data != null&&!compareLists(mRecipesMainAdapter.getRecipesListShown(),recipes.data)) {
                    mRecipesMainAdapter.setRecipesListToShow(recipes.data);
                    todoListEmptyView.setVisibility(View.GONE);
                }
            }
        });


    }
    public boolean compareLists(List<Recipe> baseList,List<Recipe> newList){
        boolean areSame=true;
        if (baseList==null){
            areSame=false;
        }else {
            for (Recipe item : newList) {
                if (!baseList.contains(item)) {
                    areSame = false;
                }
            }
        }
        Timber.d("tttttttttt"+String.valueOf(areSame));
        return areSame;
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getData();
    }

    // swipe to refresh listener
    @Override
    public void onRefresh() {
        // Fetching data from server
        viewModel.getData();
    }



    @Override
    public void onClick(Recipe selectedRecipeItem) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Constants.SELECTEDRECIPE, selectedRecipeItem);
        startActivity(intent);
    }

}
