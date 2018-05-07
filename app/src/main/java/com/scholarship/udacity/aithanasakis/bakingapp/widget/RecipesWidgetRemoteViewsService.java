package com.scholarship.udacity.aithanasakis.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

/**
 * Created by 3piCerberus on 04/05/2018.
 */

public class RecipesWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipesWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public static void updateWidget(Context context, Recipe selectedRecipe) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipesWidgetProvider.class));
        RecipesWidgetProvider.updateWidgets(context, appWidgetManager, appWidgetIds);
    }

}