package com.raymundo.crypto.service;

import com.raymundo.crypto.dto.MakeExchangeDto;
import com.raymundo.crypto.dto.SecretKeyDto;
import com.raymundo.crypto.dto.WithdrawDto;
import com.raymundo.crypto.entity.CurrencyEntity;
import com.raymundo.crypto.entity.CurrencyPriceEntity;
import com.raymundo.crypto.entity.ExchangeEntity;
import com.raymundo.crypto.entity.UserEntity;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.exception.WithdrawException;
import com.raymundo.crypto.repository.CurrencyPriceRepository;
import com.raymundo.crypto.repository.CurrencyRepository;
import com.raymundo.crypto.repository.ExchangeRepository;
import com.raymundo.crypto.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.ObjectError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OperationsService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found for this key";
    private static final String LOW_BALANCE_MESSAGE = "Balance is too low";
    private static final String CURRENCY_NOT_FOUND_MESSAGE = "There is no such a currency";
    private static final String CURRENCY_NOT_SET_MESSAGE = "The price for this currency is not set";
    private static final String EXCHANGE_NOT_FOUND_MESSAGE = "There is no such an exchange";

    private UserRepository userRepository;
    private CurrencyRepository currencyRepository;
    private ExchangeRepository exchangeRepository;
    private CurrencyPriceRepository currencyPriceRepository;

    public Map<String, String> makeDeposit(SecretKeyDto secretKeyDto, Map<String, String> value, BindingResult bindingResult) throws UserNotFoundException, ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        UserEntity user = userRepository.getUserBySecretKey(secretKeyDto.getSecretKey()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        Map<String, String> result = new HashMap<>();
        value.forEach((k, v) -> {
            Optional<CurrencyEntity> optional = currencyRepository.getCurrencyByUserAndName(user, k);
            CurrencyEntity currency;
            if (optional.isPresent()) {
                currency = optional.get();
                currency.setValue(currency.getValue() + Double.parseDouble(v));
            } else {
                currency = new CurrencyEntity();
                currency.setName(k);
                currency.setValue(Double.parseDouble(v));
                currency.setUser(user);
            }
            result.put(currency.getName(), String.valueOf(currency.getValue()));
            currencyRepository.save(currency);

        });
        makeOperation(user);
        return result;
    }

    public Map<String, String> makeWithdraw(WithdrawDto withdraw, BindingResult bindingResult) throws UserNotFoundException, WithdrawException, ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        UserEntity user = userRepository.getUserBySecretKey(withdraw.getSecretKey()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        Optional<CurrencyEntity> optional = currencyRepository.getCurrencyByUserAndName(user, withdraw.getCurrency());
        if (optional.isPresent()) {
            if (optional.get().getValue() >= withdraw.getCount()) {
                CurrencyEntity currencyEntity = optional.get();
                currencyEntity.setValue(currencyEntity.getValue() - withdraw.getCount());
            } else
                throw new WithdrawException(LOW_BALANCE_MESSAGE);
        } else
            throw new WithdrawException(CURRENCY_NOT_FOUND_MESSAGE);
        currencyRepository.save(optional.get());

        makeOperation(user);

        return Map.of(optional.get().getName(), String.valueOf(optional.get().getValue()));
    }

    public Map<String, String> makeExchange(MakeExchangeDto exchangeDto,
                                            BindingResult bindingResult) throws UserNotFoundException, ExchangeException, WithdrawException, ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        UserEntity user = userRepository.getUserBySecretKey(exchangeDto.getSecretKey()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        ExchangeEntity exchange = exchangeRepository.getExchangeByCurrencyName(exchangeDto.getCurrencyFrom())
            .orElseThrow(() -> new ExchangeException(EXCHANGE_NOT_FOUND_MESSAGE));
        CurrencyPriceEntity currencyPrice = currencyPriceRepository.getCurrencyPriceByExchangeAndName(exchange, exchangeDto.getCurrencyTo())
            .orElseThrow(() -> new ExchangeException(CURRENCY_NOT_SET_MESSAGE));
        makeWithdraw(new WithdrawDto() {{
            setSecretKey(exchangeDto.getSecretKey());
            setCurrency(exchangeDto.getCurrencyFrom());
            setCount(exchangeDto.getAmount());
        }}, bindingResult);
        Double amountTo = currencyPrice.getPrice() * exchangeDto.getAmount();
        makeDeposit(new SecretKeyDto() {{
            setSecretKey(user.getSecretKey());
        }}, Map.of(exchangeDto.getCurrencyTo(), String.valueOf(amountTo)), bindingResult);

        makeOperation(user);

        return Map.of("currency_from", exchangeDto.getCurrencyFrom(), "currency_to", exchangeDto.getCurrencyTo(),
            "amount_from", String.valueOf(exchangeDto.getAmount()), "amount_to", String.valueOf(amountTo));
    }

    private void makeOperation(UserEntity user) {
        user.setLastOperationDate(new Date(System.currentTimeMillis()));
        userRepository.save(user);
    }

}