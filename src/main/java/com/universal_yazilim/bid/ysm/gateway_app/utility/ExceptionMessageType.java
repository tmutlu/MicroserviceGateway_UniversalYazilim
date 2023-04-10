package com.universal_yazilim.bid.ysm.gateway_app.utility;

public enum ExceptionMessageType
{
    FIND_BY_ID("Entity not found."),
    ADD("Add operation is not successful."),
    DELETE("Delete operation is not successful."),
    UPDATE("Update operation is not successful."),
    GET_ALL("Entity list is not found.");

    private String value;

    ExceptionMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
