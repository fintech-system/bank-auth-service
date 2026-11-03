package kz.fintech.bank.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kz.fintech.bank.auth.entity.enums.Role;

import java.util.UUID;

public final class AuthDto {

    private AuthDto() {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record RegisterRequest(
            @NotBlank @Size(max = 80) String firstName,
            @NotBlank @Size(max = 80) String lastName,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 72) String password
    ) {
    }

    public record UserResponse(
            UUID userId,
            String email,
            String firstName,
            String lastName,
            String role
    ) {
    }
}
