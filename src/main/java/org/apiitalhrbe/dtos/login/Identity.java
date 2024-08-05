package org.apiitalhrbe.dtos.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "identity_type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UsernameIdentity.class, name = "USERNAME"),
        @JsonSubTypes.Type(value = EmailIdentity.class, name = "EMAIL")
})

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identity {

    @Schema(
            title = "Type of identity used to log in",
            description = "The type of identity provided for logging in",
            example = "Username or Email",
            nullable = false
    )
    @NotNull(message = "Identity type cannot be null")
    @JsonProperty("identity_type")
    private IdentityType identityType;
}
