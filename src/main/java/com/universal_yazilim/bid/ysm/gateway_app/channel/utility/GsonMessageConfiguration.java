package com.universal_yazilim.bid.ysm.gateway_app.channel.utility;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ******18 -> SecurityConfig
@Configuration
public class GsonMessageConfiguration
{
    // bu adaptor ile "serialization" ve "deserialization" anlatılır -> registerTypeAdapter() metodu ile
    // ISO tarih formatı, önceden tanımlanmış bir tarih biçimlendiricisidir.
    @Bean
    public GsonBuilder gsonBuilder()
    {
        GsonBuilder gsonBuilder =  new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>()
        {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context)
            {
                return src == null ? null : new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>()
        {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        });

        return gsonBuilder;
    }

    @Bean
    public Gson gson(GsonBuilder builder)
    {
        return gsonBuilder().create();
    }

    // varsayılan mesaj dönüştürücüyü özellestirmek icin
    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson)
    {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }
}
