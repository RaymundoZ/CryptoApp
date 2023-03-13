package com.raymundo.crypto.controller;

import com.raymundo.crypto.dto.request.DepositRequest;
import com.raymundo.crypto.dto.request.MakeExchangeRequest;
import com.raymundo.crypto.dto.response.DepositResponse;
import com.raymundo.crypto.dto.response.ErrorResponse;
import com.raymundo.crypto.dto.request.WithdrawRequest;
import com.raymundo.crypto.dto.response.MakeExchangeResponse;
import com.raymundo.crypto.dto.response.WithdrawResponse;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.exception.WithdrawException;
import com.raymundo.crypto.service.OperationsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@AllArgsConstructor
public class OperationsController {

    private OperationsService operationsService;

    @PostMapping(value = "/deposit")
    public ResponseEntity<DepositResponse> makeDeposit(@RequestBody @Valid DepositRequest depositDto,
                                                       BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        return ResponseEntity.ok(operationsService.makeDeposit(depositDto, bindingResult));
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<WithdrawResponse> makeWithDraw(@RequestBody @Valid WithdrawRequest withdrawDto,
                                                         BindingResult bindingResult) throws UserNotFoundException, WithdrawException, ValidationException {
        return ResponseEntity.ok(operationsService.makeWithdraw(withdrawDto, bindingResult));
    }

    @PostMapping(value = "/exchange")
    public ResponseEntity<MakeExchangeResponse> makeExchange(@RequestBody @Valid MakeExchangeRequest makeExchangeDto,
                                                             BindingResult bindingResult) throws UserNotFoundException, ExchangeException, WithdrawException, ValidationException {
        return ResponseEntity.ok(operationsService.makeExchange(makeExchangeDto, bindingResult));
    }

    @ExceptionHandler(value = {UserNotFoundException.class, WithdrawException.class, ExchangeException.class, ValidationException.class})
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponseDto = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(new Date())
                .build();
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

}