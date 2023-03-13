package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class WithdrawRequest {

    @JsonUnwrapped
    SecretKeyDto secretKey;

    @NotEmpty(message = "You should provide a currency")
    String currency;

    @Min(value = 0, message = "Count should not be less than 0")
    Double count;

}