package com.raymundo.crypto.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangeExchangeDto {

    @NotEmpty(message = "Secret key should not be empty")
    private String secretKey;

    @NotEmpty(message = "Base currency should not be empty")
    private String baseCurrency;

}