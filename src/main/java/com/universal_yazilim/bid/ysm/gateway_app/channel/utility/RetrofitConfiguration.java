package com.universal_yazilim.bid.ysm.gateway_app.channel.utility;

import com.google.gson.Gson;

import com.universal_yazilim.bid.ysm.gateway_app.channel.repository.ProductCallable;
import com.universal_yazilim.bid.ysm.gateway_app.channel.repository.TransactionCallable;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

// ******17 -> GsonMessageConfiguration sınıfı eklendi.
@Configuration
public class RetrofitConfiguration
{
    @Value("${retrofit.timeout}")
    private Long TIMEOUT_IN_SECS;

    // 1- varsayilan bir OkHttpClient.Builder
    // 2- Builder uzerinden OkHttpClient 
    // 3- RetrofitBuilder

    @Bean
    public TransactionCallable createTransactionCallable(Retrofit.Builder secureKeyBuilder, @Value("${transaction.service.url}") String baseUrl)
    {
        return secureKeyBuilder.baseUrl(baseUrl).build().create(TransactionCallable.class);
    }

    @Bean
    public ProductCallable createProductCallable(Retrofit.Builder secureKeyBuilder, @Value("${product.service.url}") String baseUrl)
    {
        return secureKeyBuilder.baseUrl(baseUrl).build().create(ProductCallable.class);
    }

    // ******c SecureKeyClient'i kullanarak -> Retrofit.Builder -> yeni bir Retrofit nesnesi oluşturmak
    @Bean
    public Retrofit.Builder secureKeyBuilder(OkHttpClient secureKeyClient, Gson gson)
    {
        return new Retrofit.Builder()
                .client(secureKeyClient)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    // ******b (Builder uzerinden guvenli bir OkHttpClient(Factory for calls, which can be used to send HTTP requests and read their responses.))
    @Bean
    public OkHttpClient secureKeyClient(@Value("${service.security.secure-key-username}")String secureKeyUsername,
                                        @Value("${service.security.secure-key-password}")String secureKeyPassword)
    {
        return createDefaultClientBuilder().addInterceptor(interceptor -> interceptor.proceed(interceptor.request().newBuilder()
                                           .header("Authorization", Credentials.basic(secureKeyUsername, secureKeyPassword))
                                           .build())).build();

    }

    // ******a (varsayilan bir OkHttpClient.Builder)
    private OkHttpClient.Builder createDefaultClientBuilder()
    {
        //servisle kurulacak "handshake" zaman asimi
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_IN_SECS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECS, TimeUnit.SECONDS);

    }
}
