package com.raymundo.crypto.repository;

import com.raymundo.crypto.entity.CurrencyEntity;
import com.raymundo.crypto.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> getCurrencyByUserAndName(UserEntity userEntity, String name);

}