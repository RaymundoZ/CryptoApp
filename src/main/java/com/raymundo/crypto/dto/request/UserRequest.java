package com.raymundo.crypto.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserRequest {

    @NotEmpty(message = "Username should not be empty")
    String username;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    String email;

}
