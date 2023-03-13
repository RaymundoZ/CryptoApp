package com.raymundo.crypto.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class OperationAmountResponse {

    @JsonProperty(value = "transaction_count")
    Long transactionCount;

}