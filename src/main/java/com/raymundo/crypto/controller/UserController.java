package com.raymundo.crypto.controller;

import com.raymundo.crypto.dto.request.GetExchangeRequest;
import com.raymundo.crypto.dto.request.SecretKeyDto;
import com.raymundo.crypto.dto.request.UserRequest;
import com.raymundo.crypto.dto.response.BalanceResponse;
import com.raymundo.crypto.dto.response.ErrorResponse;
import com.raymundo.crypto.dto.response.GetExchangeResponse;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping(value = "/registrate")
    public ResponseEntity<SecretKeyDto> registration(@RequestBody @Valid UserRequest userDto, BindingResult bindingResult) throws ValidationException {
        return ResponseEntity.ok(userService.registrateUser(userDto, bindingResult));
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<BalanceResponse> getBalance(@RequestBody @Valid SecretKeyDto secretKeyDto,
                                                      BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        return ResponseEntity.ok(userService.getBalance(secretKeyDto, bindingResult));
    }

    @GetMapping(value = "/available_exchanges")
    public ResponseEntity<GetExchangeResponse> getAllExchanges(@RequestBody @Valid GetExchangeRequest getExchangeDto,
                                                               BindingResult bindingResult) throws ExchangeException, ValidationException {
        return ResponseEntity.ok(userService.getAllExchanges(getExchangeDto, bindingResult));
    }

    @ExceptionHandler(value = {UserNotFoundException.class, ExchangeException.class, ValidationException.class})
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponseDto = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(new Date())
                .build();
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

}
