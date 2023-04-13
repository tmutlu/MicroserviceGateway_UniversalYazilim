package com.universal_yazilim.bid.ysm.gateway_app.channel.utility;

import com.universal_yazilim.bid.ysm.gateway_app.utility.Util;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;


// ******15 -> TransactionService
public final class RetrofitUtil
{
    private RetrofitUtil() {}

    public static <E> E callGenericBlock(Call<E> request)
    {
        try
        {
            Response<E> response = request.execute();

            if(!response.isSuccessful())
            {
                System.err.println("Unsuccessful response." + "Response error: " + response.errorBody().string());
            }

            return response.body();
        }
        catch (IOException e)
        {
            Util.showGeneralExceptionInfo(e);
            return null;
        }
        catch (RuntimeException e)
        {
            Util.showGeneralExceptionInfo(e);
            return null;
        }
    }
}
