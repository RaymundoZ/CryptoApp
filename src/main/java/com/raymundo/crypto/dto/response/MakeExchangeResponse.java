package com.raymundo.crypto.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MakeExchangeResponse {

    @JsonProperty(value = "currency_from")
    String currencyFrom;

    @JsonProperty(value = "currency_to")
    String currencyTo;

    @JsonProperty(value = "amount_from")
    Double amountFrom;

    @JsonProperty(value = "amount_to")
    Double amountTo;

}