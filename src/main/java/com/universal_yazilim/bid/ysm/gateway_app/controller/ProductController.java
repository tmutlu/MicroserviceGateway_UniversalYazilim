package com.universal_yazilim.bid.ysm.gateway_app.controller;

import com.google.gson.JsonElement;
import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// ******11 -> AbstractProductService
@RequestMapping("gateway/product")
@RestController
public class ProductController
{
    @Autowired
    private AbstractProductService productService;

    @GetMapping
    public ResponseEntity<List<JsonElement>> getAll()
    {
        return ResponseEntity.ok(productService.getAll());
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteByID(@PathVariable(name = "id") Integer productID)
    {
        productService.deleteByID(productID);

        return new ResponseEntity("Product(product ID:" + productID + ") is deleted.", HttpStatus.OK);
    }

    /*
        Kural: Diğer REST api'lerin varlık sınıfı olarak
        JsonElement kullanılır.
     */
    @PostMapping
    public ResponseEntity save(@RequestBody JsonElement product)
    {
        return ResponseEntity.ok(productService.save(product));
    }
}
