package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("document_number")
        private String documentNumber;

        @JsonProperty("workstation_id")
        private Long workstationId;

        @JsonProperty("department_id")
        private Long departmentId;

        @JsonProperty("start_date")
        private LocalDate startDate;

        @JsonProperty("reason_for_incorporation")
        private String reasonForIncorporation;

        @JsonProperty("workload")
        private Integer workload;

        @JsonProperty("workday_type")
        private String workdayType;

        @JsonProperty("contract_type")
        private String contractType;

        @JsonProperty("contract_from")
        private LocalDate contractFrom;

        @JsonProperty("contract_to")
        private LocalDate contractTo;

        @JsonProperty("chief_remarks_id")
        private Long chiefRemarksId;

        @JsonProperty("remuneration_id")
        private Long remunerationId;

        @JsonProperty("other_remarks_id")
        private Long otherRemarksId;

        @JsonProperty("authorization_id")
        private Long authorizationId;

        @JsonProperty("referent_id")
        private Long referentId;

        @JsonProperty("is_referent")
        private Boolean isReferent;

        @JsonProperty("is_active")
        private Boolean isActive;

}
