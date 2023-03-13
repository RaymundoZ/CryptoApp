package com.raymundo.crypto.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Value
@Builder
@Jacksonized
public class ErrorResponse {

    String message;

    @JsonFormat(pattern = "dd.MM.yyyy, HH:mm:ss", timezone = "Europe/Moscow")
    Date timestamp;

}