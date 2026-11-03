package kz.fintech.bank.auth.entity;

import jakarta.persistence.*;
import kz.fintech.bank.auth.entity.enums.AccountStatus;
import kz.fintech.bank.auth.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
public class AuthUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AccountStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

}
