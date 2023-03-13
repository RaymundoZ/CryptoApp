package com.raymundo.crypto.util;

import com.raymundo.crypto.dto.request.UserRequest;
import com.raymundo.crypto.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private static final String INVALID_USERNAME_MESSAGE = "This username is already in use";
    private static final String INVALID_EMAIL_MESSAGE = "This email is already in use";

    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequest userDto = (UserRequest) target;
        if (userRepository.getUserByUsername(userDto.getUsername()).isPresent())
            errors.rejectValue("username", "", INVALID_USERNAME_MESSAGE);
        if (userRepository.getUserByEmail(userDto.getEmail()).isPresent())
            errors.rejectValue("email", "", INVALID_EMAIL_MESSAGE);
    }
}