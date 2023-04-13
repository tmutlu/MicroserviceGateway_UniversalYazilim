package com.universal_yazilim.bid.ysm.gateway_app.channel.repository;

import com.google.gson.JsonElement;
import org.springframework.web.bind.annotation.PathVariable;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TransactionCallable
{
    @GET("api/transaction/user/{userID}")
    Call<List<JsonElement>> getTransactionsOfUser(@Path("userID") Integer id);

    @GET("api/transaction")
    Call<List<JsonElement>> getAll();

    @DELETE("api/transaction/{id}")
    Call<String> deleteByID(@Path("id") Integer id);

    @POST("api/transaction")
    Call<JsonElement> save(@Body JsonElement transaction);
}
