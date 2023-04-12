package com.universal_yazilim.bid.ysm.gateway_app.controller;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractProductService;
import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("gateway/transaction")
@RestController
public class TransactionController
{
    @Autowired
    private AbstractTransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<JsonElement>> getAll()
    {
        return null;
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteByID(@PathVariable(name = "id") Integer transactionID)
    {
        return null;
    }

    /*
        Kural: Diğer REST api'lerin varlık sınıfı olarak
        JsonElement kullanılır.
     */
    @PostMapping
    public ResponseEntity save(@RequestBody JsonElement transaction)
    {
        return null;
    }
}
