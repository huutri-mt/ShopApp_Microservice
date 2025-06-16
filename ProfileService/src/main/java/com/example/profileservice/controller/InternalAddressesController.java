package com.example.profileservice.controller;

import com.example.profileservice.constan.UrlConstant;
import com.example.profileservice.dto.request.AddressCreationRequest;
import com.example.profileservice.service.AddressesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstant.API_V1_ADDRESS_INTERNAL)
public class InternalAddressesController {

    @Autowired
    private AddressesService addressesService;

    @PostMapping("/create")
    public ResponseEntity<String> createAddress(@RequestBody AddressCreationRequest request ) {
        // Validate the request
        return ResponseEntity.status(HttpStatus.CREATED).body(addressesService.createAddress(request));
    }
}
