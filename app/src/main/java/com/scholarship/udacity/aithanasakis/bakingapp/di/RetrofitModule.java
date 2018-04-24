package com.scholarship.udacity.aithanasakis.bakingapp.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scholarship.udacity.aithanasakis.bakingapp.network.RecipeApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 3piCerberus on 24/04/2018.
 */

@Module(includes = OkHttpClientModule.class)
public class RetrofitModule {
    final static String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/";

    @Provides
    @Singleton
    public RecipeApi getWaterMeterService(Retrofit getClient){
        return getClient.create(RecipeApi.class);
    }

    @Provides
    public Retrofit getClient(OkHttpClient okHttpClient,
                              GsonConverterFactory gsonConverterFactory, Gson gson){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}

