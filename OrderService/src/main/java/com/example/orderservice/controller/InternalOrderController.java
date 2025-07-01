package com.example.orderservice.controller;

import com.example.orderservice.constan.UrlConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
    @RequestMapping(UrlConstant.API_V1_ORDER_INTERNAL)
public class InternalOrderController {

}
