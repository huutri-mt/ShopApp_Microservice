package com.example.profileservice.service.Impl;

import com.example.profileservice.dto.request.AddressCreationRequest;
import com.example.profileservice.dto.request.AddressUpdateRequest;
import com.example.profileservice.dto.response.AddressResponse;
import com.example.profileservice.entity.Addresses;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.exception.ErrorCode;
import com.example.profileservice.repository.AddressesRepository;
import com.example.profileservice.repository.UserProfileRepository;
import com.example.profileservice.service.AddressesService;
import com.example.profileservice.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@Primary
@Slf4j
public class AddressesServiceImpl implements AddressesService {
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    public String createAddress(AddressCreationRequest request) {
        if (request == null) {
            throw new AppException(ErrorCode.INVALID_ADDRESS_DATA);
        }
        Integer userId = SecurityUtil.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        List<Addresses> addresses = addressesRepository.findByUserProfile_Id(userId);
        boolean isDuplicate = addresses.stream().anyMatch(addr ->
                addr.getContactName().equals(request.getContactName()) &&
                        addr.getContactPhone().equals(request.getContactPhone()) &&
                        addr.getAddressLine1().equals(request.getAddressLine1()) &&
                        addr.getAddressLine2().equals(request.getAddressLine2()) &&
                        addr.getCity().equals(request.getCity()) &&
                        addr.getProvince().equals(request.getProvince()) &&
                        addr.getPostalCode().equals(request.getPostalCode()) &&
                        addr.getCountry().equals(request.getCountry())
        );

        if (isDuplicate) {
            throw new AppException(ErrorCode.ADDRESS_ALREADY_EXISTS);
        }

        addressesRepository.save(
            Addresses.builder()
                    .userProfile(userProfile)
                    .contactName(request.getContactName())
                    .contactPhone(request.getContactPhone())
                    .addressLine1(request.getAddressLine1())
                    .addressLine2(request.getAddressLine2())
                    .city(request.getCity())
                    .province(request.getProvince())
                    .postalCode(request.getPostalCode())
                    .country(request.getCountry())
                    .build()


        );
        return "Address created successfully";
    }

    public String updateAdderss(Integer addressId, AddressUpdateRequest request) {
        if (request == null) {
            throw new AppException(ErrorCode.INVALID_ADDRESS_DATA);
        }
        log.info("Updating address with id {}", addressId);

        Addresses address =  addressesRepository.findById(addressId).orElseThrow(
            () -> new AppException(ErrorCode.ADDRESS_NOT_FOUND)
        );

        if (request.getContactName() != null) {
            address.setContactName(request.getContactName());
        }
        if (request.getContactPhone() != null) {
            address.setContactPhone(request.getContactPhone());
        }
        if (request.getAddressLine1() != null) {
            address.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            address.setAddressLine2(request.getAddressLine2());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getProvince() != null) {
            address.setProvince(request.getProvince());
        }
        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        
        addressesRepository.save(address);
        return "Address updated successfully";
    }


    public AddressResponse getAddressById(Integer addressId) {
        if (addressId == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        Addresses address = addressesRepository.findAddressById(addressId);
        if (address == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        log.info("Retrieved address with id {}", addressId);
        return AddressResponse.builder()
                .contactName(address.getContactName())
                .contactPhone(address.getContactPhone())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .isDefault(address.getIsDefault() != null ? address.getIsDefault() : false)
                .build();

    }

    public Boolean checkAddress(Integer userId, Integer addressId) {
        if (userId == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (addressId == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        Addresses address = addressesRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        return Integer.valueOf(address.getUserProfile().getId()).equals(userId);

    }


    public String deleteAddress(Integer addressId) {
        Addresses address = addressesRepository.findById(addressId).orElseThrow(
                () -> new AppException(ErrorCode.ADDRESS_NOT_FOUND)
        );
        addressesRepository.delete(address);
        return "Address deleted successfully";
    }


}
