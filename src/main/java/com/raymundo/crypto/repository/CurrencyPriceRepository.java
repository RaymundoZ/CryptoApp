package com.raymundo.crypto.repository;

import com.raymundo.crypto.entity.CurrencyPriceEntity;
import com.raymundo.crypto.entity.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyPriceRepository extends JpaRepository<CurrencyPriceEntity, Long> {

    Optional<CurrencyPriceEntity> getCurrencyPriceByExchangeAndName(ExchangeEntity exchange, String name);

}