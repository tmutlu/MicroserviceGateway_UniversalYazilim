package com.universal_yazilim.bid.ysm.gateway_app.utility;

public final class Util
{
    private Util() {}

    public static void showGeneralExceptionInfo(Exception e)
    {
        System.err.println(createGeneralExceptionInfo(e));
    }

    public static String createGeneralExceptionInfo(Exception e)
    {
        return e.getClass().getSimpleName() + " has been occured. Exception message: " + e.getMessage();
    }
}
