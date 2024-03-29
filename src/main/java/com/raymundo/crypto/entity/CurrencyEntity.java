package com.raymundo.crypto.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_currency")
@Data
@NoArgsConstructor
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonValue
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne
    private UserEntity user;

}
