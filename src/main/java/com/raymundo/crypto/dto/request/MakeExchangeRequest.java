package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MakeExchangeRequest {

    @JsonUnwrapped
    SecretKeyDto secretKey;

    @NotEmpty(message = "Currency_from should not be empty")
    @JsonProperty(value = "currency_from")
    String currencyFrom;

    @NotEmpty(message = "Currency_to should not be empty")
    @JsonProperty(value = "currency_to")
    String currencyTo;

    @Min(value = 0, message = "Amount should not be less than 0")
    Double amount;

}