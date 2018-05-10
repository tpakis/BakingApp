package com.scholarship.udacity.aithanasakis.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Ingredient;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.room.RecipesDAO;

import java.util.List;


/**
 * Created by 3piCerberus on 04/05/2018.
 */

public class RecipesWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private RecipesDAO recipesDAO;
    private List<Ingredient> ingredients;
    public RecipesWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        recipesDAO = BakingApplication.getMyApplication().getMainActivityViewModelComponent().getRecipeDAO();
    }

    @Override
    public void onCreate() {

    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    /*returns the number of records in the list. (
    In our case, the number of task items that need to be displayed in the app widget)*/
    @Override
    public int getCount() {
        return ingredients.size();
    }
/*
*  returns the number of types of views we have in ListView. In our case, we have same view types in each ListView item so we return 1 there.*/
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public void onDestroy() {

    }
    /*onDataSetChanged is called whenever the appwidget is updated.*/
    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();
        Recipe selectedRecipe = recipesDAO.getEntryForWidget();
        if (selectedRecipe == null){
            selectedRecipe = recipesDAO.getSpecifigEntryById(1);
        }
        ingredients = selectedRecipe.getIngredients();
        Binder.restoreCallingIdentity(identityToken);

    }
    /*handles all the processing work. It returns a RemoteViews object which in our case is the single list item.*/
    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                ingredients == null ) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipes_list_item);
        rv.setTextViewText(R.id.widget_item_ingredient_name_label, ingredients.get(position).getIngredient());

        return rv;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

}
