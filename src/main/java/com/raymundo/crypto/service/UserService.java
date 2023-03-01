package com.raymundo.crypto.service;

import com.raymundo.crypto.dto.GetExchangeDto;
import com.raymundo.crypto.dto.SecretKeyDto;
import com.raymundo.crypto.dto.UserDto;
import com.raymundo.crypto.entity.ExchangeEntity;
import com.raymundo.crypto.entity.UserEntity;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.repository.ExchangeRepository;
import com.raymundo.crypto.repository.UserRepository;
import com.raymundo.crypto.util.SecretKeyGenerator;
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
    private SecretKeyGenerator secretKeyGenerator;
    private UserValidator userValidator;

    public SecretKeyDto registrateUser(UserDto user, BindingResult bindingResult) throws ValidationException {
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());


        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        String key = secretKeyGenerator.generate();
        userEntity.setSecretKey(key);
        userRepository.save(userEntity);
        return new SecretKeyDto() {{setSecretKey(key);}};
    }

    public Map<String, String> getBalance(SecretKeyDto secretKeyDto, BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        UserEntity user = userRepository.getUserBySecretKey(secretKeyDto.getSecretKey()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        Map<String, String> result = new HashMap<>();
        user.getCurrencies().forEach(c -> result.put(c.getName(), String.valueOf(c.getValue())));
        return result;
    }

    public Map<String, String> getAllExchanges(GetExchangeDto exchangeDto, BindingResult bindingResult) throws ExchangeException, ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        ExchangeEntity exchange = exchangeRepository.getExchangeByCurrencyName(exchangeDto.getCurrency())
            .orElseThrow(() -> new ExchangeException(EXCHANGE_NOT_FOUND_MESSAGE));
        Map<String, String> result = new HashMap<>();
        exchange.getCurrencyPriceEntities().forEach(cp -> result.put(cp.getName(), String.valueOf(cp.getPrice())));
        return result;
    }


}