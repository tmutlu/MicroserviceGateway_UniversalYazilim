package com.universal_yazilim.bid.ysm.gateway_app.model.service;

import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService extends AbstractTransactionService
{
    @Override
    public List<JsonElement> getAll() {
        return null;
    }

    @Override
    public JsonElement save(JsonElement entity) {
        return null;
    }

    @Override
    public void deleteByID(Integer id) {

    }
}
