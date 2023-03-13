package com.raymundo.crypto.service;

import com.raymundo.crypto.dto.request.AllCurrencyValueRequest;
import com.raymundo.crypto.dto.request.ChangeExchangeRequest;
import com.raymundo.crypto.dto.request.OperationAmountRequest;
import com.raymundo.crypto.dto.response.AllCurrencyValueResponse;
import com.raymundo.crypto.dto.response.ChangeExchangeResponse;
import com.raymundo.crypto.dto.response.OperationAmountResponse;
import com.raymundo.crypto.entity.CurrencyEntity;
import com.raymundo.crypto.entity.CurrencyPriceEntity;
import com.raymundo.crypto.entity.ExchangeEntity;
import com.raymundo.crypto.exception.InvalidDateException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.repository.CurrencyPriceRepository;
import com.raymundo.crypto.repository.CurrencyRepository;
import com.raymundo.crypto.repository.ExchangeRepository;
import com.raymundo.crypto.repository.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class AdminService {

    private static final String INVALID_DATE_MESSAGE = "Provided invalid date format";

    private ExchangeRepository exchangeRepository;
    private CurrencyPriceRepository currencyPriceRepository;
    private CurrencyRepository currencyRepository;
    private OperationRepository operationRepository;

    public ChangeExchangeResponse changeExchange(ChangeExchangeRequest changeExchangeDto, BindingResult bindingResult) throws ValidationException {
        validate(bindingResult);
        ExchangeEntity exchange = exchangeRepository.getExchangeByCurrencyName(changeExchangeDto.getBaseCurrency()).orElseGet(() -> {
            ExchangeEntity exchangeEntity = new ExchangeEntity();
            exchangeEntity.setCurrencyName(changeExchangeDto.getBaseCurrency());
            exchangeEntity.setCurrencyPriceEntities(new ArrayList<>());
            exchangeRepository.save(exchangeEntity);
            return exchangeEntity;
        });
        changeExchangeDto.getValues().forEach((k, v) -> {
            if (!k.equals("secret_key")) {
                Optional<CurrencyPriceEntity> optional = currencyPriceRepository.getCurrencyPriceByExchangeAndName(exchange, k);
                CurrencyPriceEntity priceEntity;
                if (optional.isPresent()) {
                    priceEntity = optional.get();
                    priceEntity.setPrice(Double.parseDouble(v));
                } else {
                    priceEntity = new CurrencyPriceEntity();
                    priceEntity.setName(k);
                    priceEntity.setPrice(Double.parseDouble(v));
                    priceEntity.setExchange(exchange);
                }
                exchange.getCurrencyPriceEntities().add(priceEntity);
                currencyPriceRepository.save(priceEntity);
            }
        });

        Map<String, String> result = new HashMap<>();
        exchange.getCurrencyPriceEntities().forEach(cp -> result.put(cp.getName(), String.valueOf(cp.getPrice())));
        return ChangeExchangeResponse.builder()
                .values(result)
                .build();
    }

    public AllCurrencyValueResponse calcAllCurrencyValue(AllCurrencyValueRequest allCurrencyValueDto, BindingResult bindingResult) throws ValidationException {
        validate(bindingResult);
        Double sum = currencyRepository.findAll().stream()
                .filter(c -> c.getName().equals(allCurrencyValueDto.getCurrency())).mapToDouble(CurrencyEntity::getValue).sum();
        return AllCurrencyValueResponse.builder()
                .values(Map.of(allCurrencyValueDto.getCurrency(), String.valueOf(sum)))
                .build();
    }

    public OperationAmountResponse getOperationsAmount(OperationAmountRequest operationAmountDto, BindingResult bindingResult) throws InvalidDateException, ValidationException {
        validate(bindingResult);
        Date dateFrom = parseDate(operationAmountDto.getDateFrom());
        Date dateTo = parseDate(operationAmountDto.getDateTo());
        long amount = operationRepository.findAll().stream().filter(o -> o.getDate().before(dateTo) && o.getDate().after(dateFrom)).count();
        return OperationAmountResponse.builder()
                .transactionCount(amount)
                .build();
    }

    private void validate(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors())
            for (ObjectError e : bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());
    }

    private Date parseDate(String string) throws InvalidDateException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date;
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e) {
            throw new InvalidDateException(INVALID_DATE_MESSAGE);
        }
        return date;
    }

}