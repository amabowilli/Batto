package com.techno.batto.App;


import com.techno.batto.Interface.UserInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user1 on 11/16/2017.
 */

public class AppConfig {
    private static Retrofit retrofit = null;
    private static UserInterface loadInterface = null;


    private static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder().baseUrl("http://52.47.70.91/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static UserInterface loadInterface() {
        if (loadInterface == null) {
            loadInterface = AppConfig.getClient().create(UserInterface.class);
        }
        return loadInterface;
    }


}
