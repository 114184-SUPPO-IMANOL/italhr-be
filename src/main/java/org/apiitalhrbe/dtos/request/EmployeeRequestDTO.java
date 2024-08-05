package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record EmployeeRequestDTO (

        @JsonProperty("person")
                @NotNull(message = "The person is required.")
        PersonRequestDTO person,

        @JsonProperty("workstation_id")
                @NotNull(message = "The workstation id is required.")
        Long workstationId,

        @JsonProperty("department_id")
                @NotNull(message = "The department id is required.")
        Long departmentId,

        @JsonProperty("start_date")
                @NotNull(message = "The start date is required.")
                @NotBlank(message = "The start date cant be blank.")
        LocalDate startDate,

        @JsonProperty("reason_for_incorporation")
                @NotNull(message = "The reason for incorporation is required.")
                @Size(max = 255, message = "The reason for incorporation must be up to 255 characters.")
        String reasonForIncorporation,

        @JsonProperty("workload")
                @NotNull(message = "The workload is required.")
                @Min(value = 1, message = "The workload must be greater than 0.")
                @Max(value = 48, message = "The workload must be less than 48.")
        Integer workload,

        @JsonProperty("workday_type")
                @NotNull(message = "The workday type is required.")
                @Size(max = 255, message = "The workday type must be up to 255 characters.")
        String workdayType,

        @JsonProperty("contract_type")
                @NotNull(message = "The contract type is required.")
                @Size(max = 255, message = "The contract type must be up to 255 characters.")
        String contractType,

        @JsonProperty("contract_from")
        LocalDate contractFrom,

        @JsonProperty("contract_to")
        LocalDate contractTo,

        @JsonProperty("chief_remarks")
                @NotNull(message = "The chief remarks is required.")
        ChiefRemarksRequestDTO chiefRemarks,

        @JsonProperty("remuneration")
                @NotNull(message = "The remuneration is required.")
        RemunerationRequestDTO remuneration,

        @JsonProperty("other_remarks")
                @NotNull(message = "The other remarks is required.")
        OtherRemarksRequestDTO otherRemarks,

        @JsonProperty("authorization")
                @NotNull(message = "The authorization is required.")
        AuthorizationRequestDTO authorization,

        @JsonProperty("referent_id")
        Long referentId,

        @JsonProperty("is_referent")
                @NotNull(message = "The is referent is required.")
        Boolean isReferent
){
}
