package com.raymundo.crypto.controller;

import com.raymundo.crypto.dto.ErrorResponseDto;
import com.raymundo.crypto.dto.GetExchangeDto;
import com.raymundo.crypto.dto.SecretKeyDto;
import com.raymundo.crypto.dto.UserDto;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping(value = "/registrate")
    public ResponseEntity<SecretKeyDto> registration(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) throws ValidationException {
        return ResponseEntity.ok(userService.registrateUser(userDto, bindingResult));
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<Map<String, String>> getBalance(@RequestBody @Valid SecretKeyDto secretKeyDto,
                                                          BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        return ResponseEntity.ok(userService.getBalance(secretKeyDto, bindingResult));
    }

    @GetMapping(value = "/available_exchanges")
    public ResponseEntity<Map<String, String>> getAllExchanges(@RequestBody @Valid GetExchangeDto getExchangeDto,
                                                               BindingResult bindingResult) throws ExchangeException, ValidationException {
        return ResponseEntity.ok(userService.getAllExchanges(getExchangeDto, bindingResult));
    }

    @ExceptionHandler(value = {UserNotFoundException.class, ExchangeException.class, ValidationException.class})
    private ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setTimestamp(System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponseDto);
    }

}
