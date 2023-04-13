package com.universal_yazilim.bid.ysm.gateway_app.model.service;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.channel.repository.ProductCallable;
import org.springframework.beans.factory.annotation.Autowired;


// ******12 -> ProductCallable
public abstract class AbstractProductService implements EntityService<JsonElement, Integer>
{
    @Autowired
    protected ProductCallable productCallable;
}
