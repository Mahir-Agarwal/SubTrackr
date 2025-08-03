package com.example.subtracker.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {
    // 🔧 Your current laptop IP when connected to mobile hotspot (change when needed)
    private static final String LOCAL_DEVICE_IP = "10.126.152.110";

    // 🔁 Emulator (10.0.2.2) used for Android Studio emulator only
    private static final String EMULATOR_IP = "10.0.2.2";

    // 🌐 Optional: Use production server URL here
    private static final String PROD_URL = "https://your-production-url.com/";

    // 🧠 Set this to true if running on real device
    private static final boolean RUNNING_ON_REAL_DEVICE = true;

    // 🧠 Set this to true if app is in production
    private static final boolean IS_PRODUCTION = false;

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // ⏱ Timeout config
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // 🌍 Select base URL based on environment
            String baseUrl;

            if (IS_PRODUCTION) {
                baseUrl = PROD_URL;
            } else if (RUNNING_ON_REAL_DEVICE) {
                baseUrl = "http://" + LOCAL_DEVICE_IP + ":8080/";
            } else {
                baseUrl = "http://" + EMULATOR_IP + ":8080/";
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // 🔚 Must end with /
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
