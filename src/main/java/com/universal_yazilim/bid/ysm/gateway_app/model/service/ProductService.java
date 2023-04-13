package com.universal_yazilim.bid.ysm.gateway_app.model.service;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.channel.utility.RetrofitUtil;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.util.List;

// ******14 -> RetrofitUtil'de callGenericBlock metodu tanımlanır (tekrara engel olmak için).
@Service
public class ProductService extends AbstractProductService
{

    @Override
    public List<JsonElement> getAll()
    {
        Call<List<JsonElement>> requestGetAll = productCallable.getAll();

        return RetrofitUtil.callGenericBlock(requestGetAll);
    }

    @Override
    public void deleteByID(Integer id)
    {
        Call<String> requestDeletedEntity = productCallable.deleteByID(id);

        RetrofitUtil.callGenericBlock(requestDeletedEntity);
    }

    @Override
    public JsonElement save(JsonElement entity)
    {
        Call<JsonElement> requestSavedEntity = productCallable.save(entity);

        return RetrofitUtil.callGenericBlock(requestSavedEntity);
    }
}
