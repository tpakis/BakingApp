package com.scholarship.udacity.aithanasakis.bakingapp.app;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.scholarship.udacity.aithanasakis.bakingapp.BuildConfig;
import com.scholarship.udacity.aithanasakis.bakingapp.di.AppComponent;
import com.scholarship.udacity.aithanasakis.bakingapp.di.AppModule;
import com.scholarship.udacity.aithanasakis.bakingapp.di.DaggerAppComponent;
import com.scholarship.udacity.aithanasakis.bakingapp.di.RetrofitModule;
import com.scholarship.udacity.aithanasakis.bakingapp.di.RoomDbModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

public class BakingApplication extends Application {

    private AppComponent mainActivityViewModelComponent;
    private static Context context;



    private static BakingApplication myApplication;

    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        myApplication=this;
        BakingApplication.context = getApplicationContext();
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
            initializeStetho();
        }
        initDagger();
    }
    public void  initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initDagger(){

        mainActivityViewModelComponent = DaggerAppComponent.builder()
                .retrofitModule(new RetrofitModule())
                .appModule(new AppModule(this))
                .roomDbModule(new RoomDbModule(this))
                .build();
    }
    public AppComponent getMainActivityViewModelComponent() {
        return mainActivityViewModelComponent;
    }

    public static BakingApplication getMyApplication() {
        return myApplication;
    }

    public static Context getAppContext() {
        return BakingApplication.context;
    }
}
