package com.example.subtracker.network;

import com.example.subtracker.entites.Subscription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @POST("api/subscriptions")
    Call<Subscription> saveSubscription(@Body Subscription subscription);

    @GET("api/subscriptions")
    Call<List<Subscription>> getAllSubscriptions();

    @DELETE("api/subscriptions/{id}")
    Call<Void> deleteSubscription(@Path("id") Long id);
}
