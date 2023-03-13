package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SecretKeyDto {

    @NotEmpty(message = "Secret key should not be empty")
    @JsonProperty(value = "secret_key")
    String secretKey;

}