package com.raymundo.crypto.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_currency_price")
@Data
@NoArgsConstructor
public class CurrencyPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    private ExchangeEntity exchange;

}