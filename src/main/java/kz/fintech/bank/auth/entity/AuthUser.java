package kz.fintech.bank.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private UUID id;
}
