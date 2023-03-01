package com.raymundo.crypto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WithdrawDto {

    @NotEmpty(message = "Secret key should not be empty")
    private String secretKey;

    @NotEmpty(message = "You should provide a currency")
    private String currency;

    @Min(value = 0, message = "Count should not be less than 0")
    private Double count;

}