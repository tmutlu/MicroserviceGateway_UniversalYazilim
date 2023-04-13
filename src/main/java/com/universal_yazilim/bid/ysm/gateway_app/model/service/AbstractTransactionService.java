package com.universal_yazilim.bid.ysm.gateway_app.model.service;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.channel.repository.TransactionCallable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractTransactionService implements EntityService<JsonElement, Integer>
{
    @Autowired
    protected TransactionCallable transactionCallable;

    public abstract List<JsonElement> findAllByUserID(Integer userID);
}
