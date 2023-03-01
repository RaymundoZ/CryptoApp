package com.raymundo.crypto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MakeExchangeDto {

    @NotEmpty(message = "Secret key should not be empty")
    private String secretKey;

    @NotEmpty(message = "Currency_from should not be empty")
    private String currencyFrom;

    @NotEmpty(message = "Currency_to should not be empty")
    private String currencyTo;

    @Min(value = 0, message = "Amount should not be less than 0")
    private Double amount;

}