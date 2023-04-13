package com.universal_yazilim.bid.ysm.gateway_app.model.service;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.channel.utility.RetrofitUtil;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.util.List;

// ******16 -> RetrofitConfiguration
@Service
public class TransactionService extends AbstractTransactionService
{

    @Override
    public List<JsonElement> findAllByUserID(Integer userID)
    {
        Call<List<JsonElement>> requestGetTransactionsOfUser = transactionCallable.getTransactionsOfUser(userID);
        return RetrofitUtil.callGenericBlock(requestGetTransactionsOfUser);
    }
    @Override
    public List<JsonElement> getAll()
    {
        Call<List<JsonElement>> requestGetAll = transactionCallable.getAll();
        return RetrofitUtil.callGenericBlock(requestGetAll);
    }

    @Override
    public void deleteByID(Integer id)
    {
        Call<String> requestDeletedEntity = transactionCallable.deleteByID(id);
        RetrofitUtil.callGenericBlock(requestDeletedEntity);
    }

    @Override
    public JsonElement save(JsonElement entity)
    {
        Call<JsonElement> requestSavedEntity = transactionCallable.save(entity);

        return RetrofitUtil.callGenericBlock(requestSavedEntity);

    }
}
