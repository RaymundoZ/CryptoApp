package com.raymundo.crypto.entity;

import com.raymundo.crypto.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "_user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 15)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 30)
    private String email;

    @Column(name = "secret_key", nullable = false, unique = true, length = 40)
    private String secretKey;

    @Column(name = "last_operation_date")
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date lastOperationDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CurrencyEntity> currencies;

}
