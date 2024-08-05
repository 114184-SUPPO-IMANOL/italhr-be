package org.apiitalhrbe.dtos.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EmailIdentity extends Identity{

    @Schema(
            title = "Email to log in",
            description = "The user's email",
            example = "p.perez@gmail.com",
            nullable = false
    )
    @NotNull
    @JsonProperty("email")
    private String email;
}
