package org.apiitalhrbe.dtos.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
    private String role;
}
