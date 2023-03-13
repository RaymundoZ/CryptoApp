package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GetExchangeRequest {

    @JsonUnwrapped
    SecretKeyDto secretKey;

    @NotEmpty(message = "Currency should not be empty")
    String currency;

}