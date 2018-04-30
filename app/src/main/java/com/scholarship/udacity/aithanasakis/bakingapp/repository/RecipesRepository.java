package com.scholarship.udacity.aithanasakis.bakingapp.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Switch;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Resource;
import com.scholarship.udacity.aithanasakis.bakingapp.model.basic.Status;
import com.scholarship.udacity.aithanasakis.bakingapp.network.RecipeApi;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (recipesListObservable.getValue()!=null){
            loadingList=recipesListObservable.getValue().data;
        }
        recipesListObservable.setValue(Resource.loading(loadingList));
        loadAllRecipesFromDB();
        getRecipesFromWeb();
    }

    public MutableLiveData<Resource<List<Recipe>>> getRecipesListObservable() {
        return recipesListObservable;
    }

    private void getRecipesFromWeb(){
        Timber.d("getRecipesFromWeb");
        recipeApi.getRecipesFromWeb().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    setRecipesListObservableStatus(Status.SUCCESS,null);
                    addRecipesToDB(response.body());
                } else {
                    // error case
                    setRecipesListObservableStatus(Status.ERROR,String.valueOf(response.code()));
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
               setRecipesListObservableStatus(Status.ERROR, t.getMessage());
            }
        });
    }

    private void addRecipesToDB(List<Recipe> items) {
        Timber.d("addRecipesToDB");
        new AsyncTask<List<Recipe>, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(List<Recipe>... params) {
                boolean needsUpdate = false;
                for (Recipe item : params[0]) {
                    //upsert implementation for future use
                    Long inserted = recipesDAO.insertEntry(item); //-1 if not inserted
                    if (inserted == -1){
                        int updated = recipesDAO.update(item);
                        if (updated > 0){
                            needsUpdate = true;
                        }
                    }else{
                        needsUpdate = true;
                    }

                }
                return needsUpdate;
            }

            @Override
            protected void onPostExecute(Boolean needUpdate) {
                if (needUpdate) {
                    loadAllRecipesFromDB();
                }
            }
        }.execute(items);
    }
    private void loadAllRecipesFromDB() {
        Timber.d("loadAllRecipesFromDB");
        new AsyncTask<Void, Void, List<Recipe>>() {
            @Override
            protected List<Recipe> doInBackground(Void...a) {
                return recipesDAO.getAllEntries();
            }

            @Override
            protected void onPostExecute(List<Recipe> results) {
               //check if there are data in the db
                if ((results != null)&&results.size()>0) {
                    setRecipesListObservableData(results, null);
                }
            }
        }.execute();
    }

    /**
     * This method changes the observable's LiveData data without changing the status
     * @param mRecipesList the data that need to be updated
     * @param message optional message for error
     */
    private void setRecipesListObservableData(List<Recipe> mRecipesList, String message) {
        Timber.d("setRecipesListObservableData");
        Status loadingStatus = Status.LOADING;
        if (recipesListObservable.getValue()!=null){
            loadingStatus=recipesListObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                recipesListObservable.setValue(Resource.loading(mRecipesList));
                break;
            case ERROR:
                recipesListObservable.setValue(Resource.error(message,mRecipesList));
                break;
            case SUCCESS:
                recipesListObservable.setValue(Resource.success(mRecipesList));
                break;
        }
    }

    /**
     * This method changes the observable's LiveData status without changing the data
     * @param status The new status of LiveData
     * @param message optional message for error
     */
    private void setRecipesListObservableStatus(Status status, String message) {
        Timber.d("setRecipesListObservableStatus");
        List<Recipe> loadingList = null;
        if (recipesListObservable.getValue()!=null){
            loadingList=recipesListObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                recipesListObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                recipesListObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
                if (loadingList!=null) {
                    recipesListObservable.setValue(Resource.success(loadingList));
                }
                break;
        }

        }
}
