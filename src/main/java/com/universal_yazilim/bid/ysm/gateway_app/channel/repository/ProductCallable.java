package com.universal_yazilim.bid.ysm.gateway_app.channel.repository;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

// ******13 -> ProductService
// Product ile ilgili olan REST API'nin çağrımı için
public interface ProductCallable
{
    @GET("api/product")
    Call<List<JsonElement>> getAll();

    @DELETE("api/product/{id}")
    Call<String> deleteByID(@Path("id") Integer id);

    @POST("api/product")
    Call<JsonElement> save(@Body JsonElement product);
}
