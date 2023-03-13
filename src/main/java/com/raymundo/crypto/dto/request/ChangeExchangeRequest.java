package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Value
@Builder
@Jacksonized
public class ChangeExchangeRequest {

    @JsonUnwrapped
    SecretKeyDto secretKey;

    @NotEmpty(message = "Base currency should not be empty")
    @JsonProperty(value = "base_currency")
    String baseCurrency;

    @Singular(value = "values")
    @JsonAnySetter
    Map<String, String> values;

}