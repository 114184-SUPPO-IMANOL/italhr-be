package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("creation_date")
    private LocalDateTime creationDate;

    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;
}
