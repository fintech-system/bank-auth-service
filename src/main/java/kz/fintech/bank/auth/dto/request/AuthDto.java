package kz.fintech.bank.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDto {

    private AuthDto() {}


    public record LoginRequest(

            @NotBlank
            @Email
            @Size(max = 160)
            String email,

            @NotBlank
            @Size(min = 8, max = 72)
            String password
    ) {
    }

}
