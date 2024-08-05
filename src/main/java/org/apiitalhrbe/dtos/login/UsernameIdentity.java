package org.apiitalhrbe.dtos.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UsernameIdentity extends Identity{

    @Schema(
            title = "Username",
            description = "The user's username",
            example = "p.perez",
            nullable = false
    )
    @NotNull(message = "Username cannot be null")
    @JsonProperty("username")
    private String username;
}
