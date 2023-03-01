package com.raymundo.crypto.controller;

import com.raymundo.crypto.dto.ErrorResponseDto;
import com.raymundo.crypto.dto.MakeExchangeDto;
import com.raymundo.crypto.dto.SecretKeyDto;
import com.raymundo.crypto.dto.WithdrawDto;
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

import java.util.Map;

@RestController
@AllArgsConstructor
public class OperationsController {

    private OperationsService operationsService;

    @PostMapping(value = "/deposit")
    public ResponseEntity<Map<String, String>> makeDeposite(@RequestBody @Valid SecretKeyDto secretKeyDto,
                                                            @RequestBody Map<String, String> value, BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        return ResponseEntity.ok(operationsService.makeDeposit(secretKeyDto, value, bindingResult));
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<Map<String, String>> makeWithDraw(@RequestBody @Valid WithdrawDto withdrawDto,
                                                            BindingResult bindingResult) throws UserNotFoundException, WithdrawException, ValidationException {
        return ResponseEntity.ok(operationsService.makeWithdraw(withdrawDto, bindingResult));
    }

    @PostMapping(value = "/exchange")
    public ResponseEntity<Map<String, String>> makeExchange(@RequestBody @Valid MakeExchangeDto makeExchangeDto,
                                                            BindingResult bindingResult) throws UserNotFoundException, ExchangeException, WithdrawException, ValidationException {
        return ResponseEntity.ok(operationsService.makeExchange(makeExchangeDto, bindingResult));
    }

    @ExceptionHandler(value = {UserNotFoundException.class, WithdrawException.class, ExchangeException.class, ValidationException.class})
    private ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

}