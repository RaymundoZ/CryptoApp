package com.raymundo.crypto.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "_exchange")
@Data
@NoArgsConstructor
public class ExchangeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_name", nullable = false, unique = true, length = 10)
    private String currencyName;

    @OneToMany(mappedBy = "exchange", cascade = CascadeType.REMOVE)
    private List<CurrencyPriceEntity> currencyPriceEntities;

}