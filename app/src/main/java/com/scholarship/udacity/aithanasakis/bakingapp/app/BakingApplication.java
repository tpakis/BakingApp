package com.scholarship.udacity.aithanasakis.bakingapp.app;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.scholarship.udacity.aithanasakis.bakingapp.BuildConfig;

import timber.log.Timber;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class BakingApplication extends Application {

    private static Context context;



    private static BakingApplication myApplication;

    public void onCreate() {
        super.onCreate();
        myApplication=this;
        BakingApplication.context = getApplicationContext();
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
           // initDaggerComponent();
            initializeStetho();

        }
    }
    public void  initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return BakingApplication.context;
    }
}
