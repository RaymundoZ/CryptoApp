package com.raymundo.crypto.dto.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Value
@Builder
@Jacksonized
public class DepositRequest {

    @JsonUnwrapped
    SecretKeyDto secretKey;

    @JsonAnySetter
    @Singular(value = "values")
    Map<String, String> values;

}