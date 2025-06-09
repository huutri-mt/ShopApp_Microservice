package com.example.profileservice.service.Impl;

import com.example.profileservice.dto.request.AddressCreationRequest;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.exception.ErrorCode;
import com.example.profileservice.service.AddressesService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AddressesServiceImpl implements AddressesService {
    public String createAddress(AddressCreationRequest request) {
        if (request == null) {
            throw new AppException(ErrorCode.INVALID_ADDRESS_DATA);
        }
        return "Address created successfully: ";
    }
}
