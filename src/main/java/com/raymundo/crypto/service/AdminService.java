package com.raymundo.crypto.service;

import com.raymundo.crypto.dto.AllCurrencyValueDto;
import com.raymundo.crypto.dto.ChangeExchangeDto;
import com.raymundo.crypto.dto.OperationAmountDto;
import com.raymundo.crypto.entity.CurrencyEntity;
import com.raymundo.crypto.entity.CurrencyPriceEntity;
import com.raymundo.crypto.entity.ExchangeEntity;
import com.raymundo.crypto.exception.InvalidDateException;
import com.raymundo.crypto.exception.ValidationException;
import com.raymundo.crypto.repository.CurrencyPriceRepository;
import com.raymundo.crypto.repository.CurrencyRepository;
import com.raymundo.crypto.repository.ExchangeRepository;
import com.raymundo.crypto.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {

    private static final String INVALID_DATE_MESSAGE = "Provided invalid date format";

    private ExchangeRepository exchangeRepository;
    private CurrencyPriceRepository currencyPriceRepository;
    private CurrencyRepository currencyRepository;
    private UserRepository userRepository;

    public Map<String, String> changeExchange(ChangeExchangeDto changeExchangeDto, Map<String, String> value, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        ExchangeEntity exchange = exchangeRepository.getExchangeByCurrencyName(changeExchangeDto.getBaseCurrency()).orElseGet(() -> {
            ExchangeEntity exchangeEntity = new ExchangeEntity();
            exchangeEntity.setCurrencyName(changeExchangeDto.getBaseCurrency());
            return exchangeEntity;
        });
        value.forEach((k, v) -> {
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
            currencyPriceRepository.save(priceEntity);

        });

        Map<String, String> result = new HashMap<>();
        exchange.getCurrencyPriceEntities().forEach(cp -> result.put(cp.getName(), String.valueOf(cp.getPrice())));
        return result;
    }

    public Map<String, String> calcAllCurrencyValue(AllCurrencyValueDto allCurrencyValueDto, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        Double sum = currencyRepository.findAll().stream()
            .filter(c -> c.getName().equals(allCurrencyValueDto.getCurrency())).mapToDouble(CurrencyEntity::getValue).sum();
        return Map.of(allCurrencyValueDto.getCurrency(), String.valueOf(sum));
    }

    public Map<String, String> getOperationsAmount(OperationAmountDto operationAmountDto, BindingResult bindingResult) throws InvalidDateException, ValidationException {
        if(bindingResult.hasErrors())
            for(ObjectError e: bindingResult.getAllErrors())
                throw new ValidationException(e.getDefaultMessage());

        long dateFrom = parseDate(operationAmountDto.getDateFrom()).getTime();
        long dateTo = parseDate(operationAmountDto.getDateTo()).getTime();
        long amount = userRepository.findAll().stream().filter(u -> u.getLastOperationDate() != null).mapToLong(u -> u.getLastOperationDate().getTime()).filter(time -> dateFrom <= time && dateTo >= time).count();
        return Map.of("transaction_count", String.valueOf(amount));
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