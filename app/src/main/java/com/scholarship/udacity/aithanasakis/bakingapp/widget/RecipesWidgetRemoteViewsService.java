package com.scholarship.udacity.aithanasakis.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by 3piCerberus on 04/05/2018.
 */

public class RecipesWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipesWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}