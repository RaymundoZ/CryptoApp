package com.raymundo.crypto.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AllCurrencyValueDto {

    @NotEmpty(message = "Secret key should not be empty")
    private String secretKey;

    @NotEmpty(message = "Currency should not be empty")
    private String currency;

}