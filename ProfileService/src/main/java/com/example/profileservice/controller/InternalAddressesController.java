package com.example.profileservice.controller;

import com.example.profileservice.constan.UrlConstant;
import com.example.profileservice.dto.response.AddressResponse;
import com.example.profileservice.service.AddressesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstant.API_V1_ADDRESS_INTERNAL)
public class InternalAddressesController {

    @Autowired()
    private AddressesService addressesService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddressByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(addressesService.getAddressByUserId(userId));
    }
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAddressExists(@RequestParam Integer userId) {
        return ResponseEntity.ok(addressesService.checkAddressExists(userId));
    }

}
