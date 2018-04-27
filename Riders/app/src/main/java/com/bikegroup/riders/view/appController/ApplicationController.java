package com.bikegroup.riders.view.appController;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApplicationController extends Application {
    /**
     * ApplicationController reference object
     */
    private static ApplicationController mApplicationController;
    private Retrofit mRetrofit;

    public static ApplicationController getInstance() {
        if (mApplicationController == null) {
            mApplicationController = new ApplicationController();
        }
        return mApplicationController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationController = this;
    }

    public Retrofit getRetrofitInstance(String strBaseUrl) {
        return new Retrofit
                .Builder()
                .baseUrl(strBaseUrl)
                .addConverterFactory
                        (MoshiConverterFactory.create())
                .build();
    }

}
