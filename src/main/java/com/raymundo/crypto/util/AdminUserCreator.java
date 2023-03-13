package com.raymundo.crypto.util;

import com.raymundo.crypto.entity.UserEntity;
import com.raymundo.crypto.repository.UserRepository;
import com.raymundo.crypto.security.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Level;

@Component
@AllArgsConstructor
@Log
public class AdminUserCreator {

    private UserRepository userRepository;
    private JwtService jwtService;

    @PostConstruct
    public void createAdminUser() {
        Optional<UserEntity> optional = userRepository.getUserByUsername("admin");
        optional.ifPresentOrElse(this::printToken, () -> {
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setEmail("admin@gmail.com");
            user.setRole(Role.ADMIN);
            printToken(user);
            userRepository.save(user);
        });
    }

    private void printToken(UserEntity userEntity) {
        String token = jwtService.generateToken(userEntity);
        log.log(Level.INFO, "Your admin secret_key is: " + token);
    }

}