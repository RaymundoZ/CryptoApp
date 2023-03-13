package com.raymundo.crypto.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Value
@Builder
@Jacksonized
public class WithdrawResponse {

    Map<String, String> values;

    @JsonAnyGetter
    public Map<String, String> getValues() {
        return values;
    }
}