package com.scholarship.udacity.aithanasakis.bakingapp.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Resource;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Status;
import com.scholarship.udacity.aithanasakis.bakingapp.network.RecipeApi;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class RecipesRepository {
    private RecipesDAO recipesDAO;
    private RecipeApi recipeApi;
    private MutableLiveData<Resource<List<Recipe>>> recipesListObservable = new MutableLiveData<Resource<List<Recipe>>>();

    @Inject
    public RecipesRepository(RecipesDAO recipesDAO, RecipeApi recipeApi){
        this.recipesDAO=recipesDAO;
        this.recipeApi=recipeApi;

    }

    public void fetchData(){
        List<Recipe> loadingList = null;
        recipesListObservable.setValue(Resource.loading(loadingList));
        loadAllRecipesFromDB();
        getRecipesFromWeb();
    }

    public MutableLiveData<Resource<List<Recipe>>> getRecipesListObservable() {
        return recipesListObservable;
    }

    private void getRecipesFromWeb(){
        recipeApi.getRecipesFromWeb().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    addRecipesToDB(response.body());

                   // measurementsListObservable.setValue(response.body());
                    //   items.addAll(response.body().getResults());
                    //   setLiveData(items);
                    // addAllToDB(itemsData);
                } else {
                    // error case
                    setRecipesListObservable(null,false,String.valueOf(response.code()));
                    switch (response.code()) {
                        case 404:
                            Timber.d("not found");
                            break;
                        case 500:
                            Timber.d("not logged in or server broken");
                            break;
                        default:
                            Timber.d("unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("asd","asd");
               setRecipesListObservable(null,false, t.getMessage());
            }
        });
    }
    private void addRecipesToDB(List<Recipe> items) {
        new AsyncTask<List<Recipe>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Recipe>... params) {
                for (Recipe item : params[0]) {
                    recipesDAO.insertEntry(item);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void a) {
                loadAllRecipesFromDB();
                //loadEntries();
            }
        }.execute(items);
    }
    private void loadAllRecipesFromDB() {
        new AsyncTask<Void, Void, List<Recipe>>() {
            @Override
            protected List<Recipe> doInBackground(Void...a) {
                return recipesDAO.getAllEntries();
            }

            @Override
            protected void onPostExecute(List<Recipe> results) {
                setRecipesListObservable(results, true, null );
                //loadEntries();
            }
        }.execute();
    }


    private boolean compareListWithLiveDataValue(List<Recipe> mRecipesList){
        boolean retValue = false;
        if (recipesListObservable.getValue() != null && recipesListObservable.getValue().data != null){
            retValue = mRecipesList.equals(recipesListObservable.getValue().data);
        }
        return retValue;
    }

    private void setRecipesListObservable(List<Recipe> mRecipesList, boolean success, String message) {
        if (success) {
            if (!compareListWithLiveDataValue(mRecipesList)) {
                recipesListObservable.setValue(Resource.success(mRecipesList));
            }
        }else{
            //mRecipesList coould be null
            recipesListObservable.setValue(Resource.error(message, mRecipesList));
        }
    }
}
