package com.raymundo.crypto.util;

import com.raymundo.crypto.dto.UserDto;
import com.raymundo.crypto.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if(userRepository.getUserByUsername(userDto.getUsername()).isPresent())
            errors.rejectValue("username", "", "This username is already in use");
        if(userRepository.getUserByEmail(userDto.getEmail()).isPresent())
            errors.rejectValue("email", "", "This email is already in use");
    }
}