package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("person")
    private PersonResponseDTO person;

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

    @JsonProperty("chief_remarks")
    private ChiefRemarksResponseDTO chiefRemarks;

    @JsonProperty("remuneration")
    private RemunerationResponseDTO remuneration;

    @JsonProperty("other_remarks")
    private OtherRemarksResponseDTO otherRemarks;

    @JsonProperty("authorization")
    private AuthorizationResponseDTO authorization;

    @JsonProperty("referent_id")
    private Long referentId;

    @JsonProperty("is_referent")
    private Boolean isReferent;

    @JsonProperty("is_active")
    private Boolean isActive;
}
