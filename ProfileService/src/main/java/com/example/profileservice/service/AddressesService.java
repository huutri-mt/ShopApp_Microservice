package com.example.profileservice.service;

import com.example.profileservice.dto.request.AddressCreationRequest;
import org.springframework.stereotype.Service;
@Service

public interface AddressesService {
    String createAddress(AddressCreationRequest request);

}
