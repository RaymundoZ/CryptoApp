package com.raymundo.crypto.controller;

import com.raymundo.crypto.dto.AllCurrencyValueDto;
import com.raymundo.crypto.dto.ChangeExchangeDto;
import com.raymundo.crypto.dto.ErrorResponseDto;
import com.raymundo.crypto.dto.OperationAmountDto;
import com.raymundo.crypto.exception.InvalidDateException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.service.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping(value = "/change_exchange")
    public ResponseEntity<Map<String, String>> changeExchange(@RequestBody @Valid ChangeExchangeDto changeExchangeDto,
                                                              @RequestBody Map<String, String> value, BindingResult bindingResult) throws ValidationException {
        return ResponseEntity.ok(adminService.changeExchange(changeExchangeDto, value, bindingResult));
    }

    @GetMapping(value = "/currency_values")
    public ResponseEntity<Map<String, String>> getAllCurencyValue(@RequestBody @Valid AllCurrencyValueDto allCurrencyValueDto, BindingResult bindingResult) throws ValidationException {
        return ResponseEntity.ok(adminService.calcAllCurrencyValue(allCurrencyValueDto, bindingResult));
    }

    @GetMapping(value = "/operations")
    public ResponseEntity<Map<String, String>> getOperationsAmount(@RequestBody @Valid OperationAmountDto operationAmountDto,
                                                                   BindingResult bindingResult) throws InvalidDateException, ValidationException {
        return ResponseEntity.ok(adminService.getOperationsAmount(operationAmountDto, bindingResult));
    }

    @ExceptionHandler(value = {InvalidDateException.class, ValidationException.class})
    private ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

}