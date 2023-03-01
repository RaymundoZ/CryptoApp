package com.raymundo.crypto.repository;

import com.raymundo.crypto.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> getUserBySecretKey(String key);

    Optional<UserEntity> getUserByUsername(String username);

    Optional<UserEntity> getUserByEmail(String email);

}