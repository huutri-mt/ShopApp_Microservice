package com.example.productservice.controller;


import com.example.productservice.constan.UrlConstant;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(UrlConstant.API_V1_PRODUCT_INTERNAL)
public class InternalProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getInternalProductInfo(@PathVariable Integer productId) {
        log.info("Fetching product with ID: {}", productId);
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/checkExists/{productId}")
    public ResponseEntity<Boolean> checkProductExists(@PathVariable Integer productId) {
        log.info("Checking if product exists with ID: {}", productId);
        boolean exists = productService.getProductById(productId) != null;
        return ResponseEntity.ok(exists);
    }

    @PutMapping ("/updateStock/{productId}")
    public ResponseEntity<String> updateProductStock(@PathVariable Integer productId, @RequestParam Integer stock) {
        log.info("Updating stock for product with ID: {}, new stock: {}", productId, stock);
        String response = productService.updateProductStock(productId, stock);
        return ResponseEntity.ok(response);
    }
}