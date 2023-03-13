package com.raymundo.crypto.controller;

import com.raymundo.crypto.dto.request.AllCurrencyValueRequest;
import com.raymundo.crypto.dto.request.ChangeExchangeRequest;
import com.raymundo.crypto.dto.request.OperationAmountRequest;
import com.raymundo.crypto.dto.response.AllCurrencyValueResponse;
import com.raymundo.crypto.dto.response.ChangeExchangeResponse;
import com.raymundo.crypto.dto.response.ErrorResponse;
import com.raymundo.crypto.dto.response.OperationAmountResponse;
import com.raymundo.crypto.exception.InvalidDateException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.service.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping(value = "/change_exchange")
    public ResponseEntity<ChangeExchangeResponse> changeExchange(@RequestBody @Valid ChangeExchangeRequest changeExchangeDto,
                                                                 BindingResult bindingResult) throws ValidationException {
        return ResponseEntity.ok(adminService.changeExchange(changeExchangeDto, bindingResult));
    }

    @GetMapping(value = "/currency_values")
    public ResponseEntity<AllCurrencyValueResponse> getAllCurencyValue(@RequestBody @Valid AllCurrencyValueRequest allCurrencyValueDto, BindingResult bindingResult) throws ValidationException {
        return ResponseEntity.ok(adminService.calcAllCurrencyValue(allCurrencyValueDto, bindingResult));
    }

    @GetMapping(value = "/operations")
    public ResponseEntity<OperationAmountResponse> getOperationsAmount(@RequestBody @Valid OperationAmountRequest operationAmountDto,
                                                                       BindingResult bindingResult) throws InvalidDateException, ValidationException {
        return ResponseEntity.ok(adminService.getOperationsAmount(operationAmountDto, bindingResult));
    }

    @ExceptionHandler(value = {InvalidDateException.class, ValidationException.class})
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponseDto = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(new Date())
                .build();
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

}