package com.scholarship.udacity.aithanasakis.bakingapp.di;

import android.content.Context;

import com.scholarship.udacity.aithanasakis.bakingapp.app.BakingApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

@Module
public class AppModule {
    BakingApplication mApplication;

    public AppModule(BakingApplication application) {
        mApplication = application;
    }

    @Provides
    public Context getAppContext(){
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    BakingApplication providesApplication() {
        return mApplication;
    }
}
