package com.raymundo.crypto.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OperationAmountDto {

    @NotEmpty(message = "Secret key should not be empty")
    private String secretKey;

    @NotEmpty(message = "Date_from should not be empty")
    private String dateFrom;

    @NotEmpty(message = "Date_to should not be empty")
    private String dateTo;

}