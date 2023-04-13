package com.universal_yazilim.bid.ysm.gateway_app.controller;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractProductService;
import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractTransactionService;
import com.universal_yazilim.bid.ysm.gateway_app.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("gateway/transaction")
@RestController
public class TransactionController
{
    @Autowired
    private AbstractTransactionService transactionService;

    /*
        @AuthenticationPrincipal ile oturum açan kullanıcıya
        Controller üzerinden kolayca erişilir.
     */
    @GetMapping
    public ResponseEntity<List<JsonElement>> getAllTransactionsOfAuthorizedUser(@AuthenticationPrincipal UserPrincipal user)
    {
        return ResponseEntity.ok(transactionService.findAllByUserID(user.getId()));
    }

    @GetMapping
    public ResponseEntity<List<JsonElement>> getAll()
    {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteByID(@PathVariable(name = "id") Integer transactionID)
    {
        transactionService.deleteByID(transactionID);

        return new ResponseEntity("Transaction(transaction ID:" + transactionID + ") is deleted.", HttpStatus.OK);
    }

    /*
        Kural: Diğer REST api'lerin varlık sınıfı olarak
        JsonElement kullanılır.
     */
    @PostMapping
    public ResponseEntity<JsonElement> save(@RequestBody JsonElement transaction)
    {
        return ResponseEntity.ok(transactionService.save(transaction));
    }
}
