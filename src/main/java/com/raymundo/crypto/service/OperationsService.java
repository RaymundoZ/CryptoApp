package com.raymundo.crypto.service;

import com.raymundo.crypto.dto.request.DepositRequest;
import com.raymundo.crypto.dto.request.MakeExchangeRequest;
import com.raymundo.crypto.dto.request.SecretKeyDto;
import com.raymundo.crypto.dto.request.WithdrawRequest;
import com.raymundo.crypto.dto.response.DepositResponse;
import com.raymundo.crypto.dto.response.MakeExchangeResponse;
import com.raymundo.crypto.dto.response.WithdrawResponse;
import com.raymundo.crypto.entity.*;
import com.raymundo.crypto.exception.ExchangeException;
import com.raymundo.crypto.exception.UserNotFoundException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.exception.WithdrawException;
import com.raymundo.crypto.repository.*;
import com.raymundo.crypto.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
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
    private OperationRepository operationRepository;
    private JwtService jwtService;

    public DepositResponse makeDeposit(DepositRequest depositDto, BindingResult bindingResult, boolean isInner) throws UserNotFoundException, ValidationException {
        validate(bindingResult);
        UserEntity user = userRepository.getUserByUsername(jwtService.getUsername(depositDto.getSecretKey().getSecretKey())).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        Map<String, String> result = new HashMap<>();
        depositDto.getValues().forEach((k, v) -> {
            if (!k.equals("secret_key")) {
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
            }
        });

        if(!isInner)
            makeOperation(user);

        return DepositResponse.builder()
                .values(result)
                .build();
    }

    public WithdrawResponse makeWithdraw(WithdrawRequest withdraw, BindingResult bindingResult, boolean isInner) throws UserNotFoundException, WithdrawException, ValidationException {
        validate(bindingResult);
        UserEntity user = userRepository.getUserByUsername(jwtService.getUsername(withdraw.getSecretKey().getSecretKey())).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
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

        if(!isInner)
            makeOperation(user);

        return WithdrawResponse.builder()
                .values(Map.of(optional.get().getName(), String.valueOf(optional.get().getValue())))
                .build();
    }

    public MakeExchangeResponse makeExchange(MakeExchangeRequest exchangeDto,
                                             BindingResult bindingResult) throws UserNotFoundException, ExchangeException, WithdrawException, ValidationException {
        validate(bindingResult);
        UserEntity user = userRepository.getUserByUsername(jwtService.getUsername(exchangeDto.getSecretKey().getSecretKey())).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        ExchangeEntity exchange = exchangeRepository.getExchangeByCurrencyName(exchangeDto.getCurrencyFrom())
                .orElseThrow(() -> new ExchangeException(EXCHANGE_NOT_FOUND_MESSAGE));
        CurrencyPriceEntity currencyPrice = currencyPriceRepository.getCurrencyPriceByExchangeAndName(exchange, exchangeDto.getCurrencyTo())
                .orElseThrow(() -> new ExchangeException(CURRENCY_NOT_SET_MESSAGE));
        makeWithdraw(WithdrawRequest.builder()
                .secretKey(SecretKeyDto.builder()
                        .secretKey(exchangeDto.getSecretKey().getSecretKey())
                        .build())
                .currency(exchangeDto.getCurrencyFrom())
                .count(exchangeDto.getAmount())
                .build(), bindingResult, true);
        Double amountTo = currencyPrice.getPrice() * exchangeDto.getAmount();
        makeDeposit(DepositRequest.builder()
                .secretKey(SecretKeyDto.builder()
                        .secretKey(exchangeDto.getSecretKey().getSecretKey())
                        .build())
                .values(exchangeDto.getCurrencyTo(), String.valueOf(amountTo))
                .build(), bindingResult, true);

        makeOperation(user);

        return MakeExchangeResponse.builder()
                .currencyFrom(exchangeDto.getCurrencyFrom())
                .currencyTo(exchangeDto.getCurrencyTo())
                .amountFrom(exchangeDto.getAmount())
                .amountTo(amountTo)
                .build();
    }

    private void validate(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors())
            for (ObjectError e : bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());
    }

    private void makeOperation(UserEntity user) {
        OperationEntity operation = new OperationEntity();
        operation.setDate(new Date());
        operation.setUser(user);
        operationRepository.save(operation);
    }

}