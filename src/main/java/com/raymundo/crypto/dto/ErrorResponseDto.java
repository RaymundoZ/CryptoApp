package com.raymundo.crypto.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {

    private String message;
    private Long timestamp;

}