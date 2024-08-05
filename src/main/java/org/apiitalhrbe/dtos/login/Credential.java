package org.apiitalhrbe.dtos.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    @Schema(
            title = "Email or Username to log in",
            description = "The user's email or username",
            example = "p.perez@gmail.com or p.perez",
            nullable = false
    )
    @NotNull(message = "Identity cannot be null")
    @JsonProperty("identity")
    private String identity;

    @NotNull(message = "Password cannot be null")
    private String password;
}
