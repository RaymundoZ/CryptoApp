package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class OperationAmountRequest {

    @JsonUnwrapped
    SecretKeyDto secretKey;

    @NotEmpty(message = "Date_from should not be empty")
    @JsonProperty(value = "date_from")
    String dateFrom;

    @NotEmpty(message = "Date_to should not be empty")
    @JsonProperty(value = "date_to")
    String dateTo;

}