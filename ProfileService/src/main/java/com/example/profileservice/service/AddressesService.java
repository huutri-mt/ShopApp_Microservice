package com.example.profileservice.service;

import com.example.profileservice.dto.request.AddressCreationRequest;
import com.example.profileservice.dto.request.AddressUpdateRequest;
import com.example.profileservice.dto.response.AddressResponse;
import com.example.profileservice.entity.Addresses;
import com.example.profileservice.entity.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service

public interface AddressesService {
    String createAddress(AddressCreationRequest request);
    String updateAdderss(Integer addressId , AddressUpdateRequest request);
    List<AddressResponse> getAddressByUserId(Integer userId);
    Boolean checkAddressExists(Integer userId);
    String deleteAddress(Integer addressId);

}
