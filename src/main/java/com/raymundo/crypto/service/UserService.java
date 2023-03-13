package com.raymundo.crypto.service;

import com.raymundo.crypto.dto.request.GetExchangeRequest;
import com.raymundo.crypto.dto.request.SecretKeyDto;
import com.raymundo.crypto.dto.request.UserRequest;
import com.raymundo.crypto.dto.response.BalanceResponse;
import com.raymundo.crypto.dto.response.GetExchangeResponse;
import com.raymundo.crypto.entity.ExchangeEntity;
import com.raymundo.crypto.entity.UserEntity;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.repository.ExchangeRepository;
import com.raymundo.crypto.repository.UserRepository;
import com.raymundo.crypto.security.JwtService;
import com.raymundo.crypto.util.Role;
import com.raymundo.crypto.util.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found for this key";
    private static final String EXCHANGE_NOT_FOUND_MESSAGE = "There is no such an exchange";

    private UserRepository userRepository;
    private ExchangeRepository exchangeRepository;
    private UserValidator userValidator;
    private JwtService jwtService;

    public SecretKeyDto registrateUser(UserRequest user, BindingResult bindingResult) throws ValidationException {
        userValidator.validate(user, bindingResult);
        validate(bindingResult);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setRole(Role.USER);
        String token = jwtService.generateToken(userEntity);
        userRepository.save(userEntity);
        return SecretKeyDto.builder()
                .secretKey(token)
                .build();
    }

    public BalanceResponse getBalance(SecretKeyDto secretKeyDto, BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        validate(bindingResult);
        UserEntity user = userRepository.getUserByUsername(jwtService.getUsername(secretKeyDto.getSecretKey())).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        Map<String, String> result = new HashMap<>();
        user.getCurrencies().forEach(c -> result.put(c.getName(), String.valueOf(c.getValue())));
        return BalanceResponse.builder()
                .values(result)
                .build();
    }

    public GetExchangeResponse getAllExchanges(GetExchangeRequest exchangeDto, BindingResult bindingResult) throws ExchangeException, ValidationException {
        validate(bindingResult);
        ExchangeEntity exchange = exchangeRepository.getExchangeByCurrencyName(exchangeDto.getCurrency())
                .orElseThrow(() -> new ExchangeException(EXCHANGE_NOT_FOUND_MESSAGE));
        Map<String, String> result = new HashMap<>();
        exchange.getCurrencyPriceEntities().forEach(cp -> result.put(cp.getName(), String.valueOf(cp.getPrice())));
        return GetExchangeResponse.builder()
                .values(result)
                .build();
    }

    private void validate(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors())
            for (ObjectError e : bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());
    }

}