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
    private Status pendingStatus;
    @Inject
    public RecipesRepository(RecipesDAO recipesDAO, RecipeApi recipeApi){
        this.recipesDAO=recipesDAO;
        this.recipeApi=recipeApi;

    }

    public void fetchData(){
        pendingStatus = Status.LOADING;
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
                    pendingStatus = Status.SUCCESS;
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
                    //start to insert item in db
                    Long inserted = recipesDAO.insertEntry(item); //-1 if not inserted
                    if (inserted == -1){
                        //check if the item in db is exactly the same with the item that we wanted
                        //to insert. If not then update item
                        if (!item.equals(recipesDAO.getSpecifigEntryById(item.getId()))){
                            int updated = recipesDAO.update(item);
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
                } else{
                    setRecipesListObservableStatus(pendingStatus,null);
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
               //if no data is stored in db then the pendingStatus will be loading
                    setRecipesListObservableData(results, null);
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
        Status loadingStatus = pendingStatus;
        if (recipesListObservable.getValue()!= null){
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
        Timber.d("setRecipesListObservableStatus"+message);
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
                //extra carefull not to be null, could implement a check but not needed now
                recipesListObservable.setValue(Resource.success(loadingList));
                break;
        }
        Timber.d("ttttttttttt"+status.toString());
        }
}
