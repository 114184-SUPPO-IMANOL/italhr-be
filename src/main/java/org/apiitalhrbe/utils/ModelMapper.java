package org.apiitalhrbe.utils;

import org.apiitalhrbe.dtos.request.*;
import org.apiitalhrbe.dtos.response.*;
import org.apiitalhrbe.entities.sql.*;

import java.time.LocalDate;

public class ModelMapper {

    public <T> T map(Object source, Class<T> destinationType) {
        if (source instanceof EmployeeEntity input && destinationType == EmployeeResponseDTO.class) {
            return destinationType.cast(mapEmployeeEntityToEmployeeResponseDTO(input));
        }
        if (source instanceof EmployeeRequestDTO input && destinationType == EmployeeEntity.class) {
            return destinationType.cast(mapEmployeeRequestDTOToEmployeeEntity(input));
        }
        if (source instanceof PersonEntity input && destinationType == PersonResponseDTO.class) {
            return destinationType.cast(mapPersonEntityToPersonResponseDTO(input));
        }
        if (source instanceof StoredDocumentRequestDTO input && destinationType == StoredDocumentEntity.class) {
            return destinationType.cast(mapStoredDocumentRequestDTOToStoredDocumentEntity(input));
        }
        if (source instanceof StoredDocumentEntity storedDocumentEntity && destinationType == StoredDocumentResponseDTO.class) {
            return destinationType.cast(mapStoredDocumentEntityToStoredDocumentResponseDTO(storedDocumentEntity));
        }
        if (source instanceof DepartmentEntity input && destinationType == DepartmentResponseDTO.class) {
            return destinationType.cast(mapDepartmentEntityToDepartmentResponseDTO(input));
        }
        if (source instanceof DepartmentRequestDTO input && destinationType == DepartmentEntity.class) {
            return destinationType.cast(mapDepartmentRequestDTOToDepartmentEntity(input));
        }
        if (source instanceof UserEntity input && destinationType == UserResponseDTO.class) {
            return destinationType.cast(mapUserEntityToUserResponseDTO(input));
        }
        if (source instanceof WorkstationRequestDTO input && destinationType == WorkstationEntity.class) {
            return destinationType.cast(mapWorkstationRequestDTOToWorkstationEntity(input));
        }
        if (source instanceof WorkstationEntity input && destinationType == WorkstationResponseDTO.class) {
            return destinationType.cast(mapWorkstationEntityToWorkstationResponseDTO(input));
        }
        if (source instanceof EmployeeEntity input && destinationType == EmployeeDetailResponseDTO.class) {
            return destinationType.cast(mapEmployeeEntityToEmployeeDetailResponseDTO(input));
        }
        if (source instanceof WorkstationEntity input && destinationType == WorkstationDetailResponseDTO.class) {
            return destinationType.cast(mapWorkstationEntityToWorkstationDetailResponseDTO(input));
        }
        return null; // TODO: implement this method
    }

    private EmployeeResponseDTO mapEmployeeEntityToEmployeeResponseDTO(EmployeeEntity input) {
        return new EmployeeResponseDTO(
                input.getId(),
                input.getPerson() == null ? null : input.getPerson().getFirstName(),
                input.getPerson() == null ? null : input.getPerson().getLastName(),
                input.getPerson() == null ? null : input.getPerson().getDocumentNumber(),
                input.getWorkstation() == null ? null : input.getWorkstation().getId(),
                null,
                input.getStartDate(),
                input.getReasonForIncorporation(),
                input.getWorkload(),
                input.getWorkdayType(),
                input.getContractType(),
                input.getContractFrom(),
                input.getContractTo(),
                input.getChiefRemarks() == null ? null : input.getChiefRemarks().getId(),
                input.getRemuneration() == null ? null : input.getRemuneration().getId(),
                input.getOtherRemarks() == null ? null : input.getOtherRemarks().getId(),
                input.getAuthorization() == null ? null : input.getAuthorization().getId(),
                input.getReferent() == null ? null : input.getReferent().getId(),
                input.getIsReferent(),
                input.getIsActive()
        );
    }

    private EmployeeEntity mapEmployeeRequestDTOToEmployeeEntity(EmployeeRequestDTO input) {
        return new EmployeeEntity(
                null,
                mapPersonRequestDTOToPersonEntity(input.person()),
                null,
                input.startDate(),
                input.reasonForIncorporation(),
                input.workload(),
                input.workdayType(),
                input.contractType(),
                input.contractFrom(),
                input.contractTo(),
                mapChiefRemarksRequestDTOToChiefRemarksEntity(input.chiefRemarks()),
                mapRemunerationRequestDTOToRemunerationEntity(input.remuneration()),
                mapOtherRemarksRequestDTOToOtherRemarksEntity(input.otherRemarks()),
                mapAuthorizationRequestDTOToAuthorizationEntity(input.authorization()),
                null,
                input.isReferent(),
                null,
                LocalDate.now(),
                null
        );
    }

    private PersonEntity mapPersonRequestDTOToPersonEntity(PersonRequestDTO input) {
        return new PersonEntity(
                null,
                input.firstName(),
                input.lastName(),
                input.documentType(),
                input.documentNumber(),
                input.gender(),
                input.maritalStatus(),
                LocalDate.now(),
                null,
                input.birthDate(),
                input.nationality(),
                true,
                mapStoredDocumentRequestDTOToStoredDocumentEntity(input.profilePicture())
        );
    }

    private StoredDocumentEntity mapStoredDocumentRequestDTOToStoredDocumentEntity(StoredDocumentRequestDTO input) {
        if (input != null) {
            return new StoredDocumentEntity(
                    input.id(),
                    input.name(),
                    input.fileName(),
                    input.extension(),
                    input.status(),
                    input.deleted(),
                    input.file(),
                    input.urlFile()
            );
        }
        return null;
    }

    private ChiefRemarksEntity mapChiefRemarksRequestDTOToChiefRemarksEntity(ChiefRemarksRequestDTO input) {
        return new ChiefRemarksEntity(
                null,
                input.membersTeamQuantity(),
                input.estimatedEffect()
        );
    }

    private OtherRemarksEntity mapOtherRemarksRequestDTOToOtherRemarksEntity(OtherRemarksRequestDTO input) {
        return new OtherRemarksEntity(
                null,
                input.manager(),
                input.requestReceived(),
                input.comments()
        );
    }

    private RemunerationEntity mapRemunerationRequestDTOToRemunerationEntity(RemunerationRequestDTO input) {
        return new RemunerationEntity(
                null,
                input.monthlySalary(),
                input.annualSalary(),
                input.comments()
        );
    }

    private AuthorizationEntity mapAuthorizationRequestDTOToAuthorizationEntity(AuthorizationRequestDTO input) {
        return new AuthorizationEntity(
                null,
                input.requestReceived(),
                input.comments(),
                input.isAuthorized()
        );
    }


    private PersonResponseDTO mapPersonEntityToPersonResponseDTO(PersonEntity input) {
        return new PersonResponseDTO(
                input.getId(),
                input.getFirstName(),
                input.getLastName(),
                input.getDocumentType(),
                input.getDocumentNumber(),
                input.getGender(),
                input.getMaritalStatus(),
                input.getBirthDate(),
                input.getNationality(),
                input.getIsActive(),
                input.getProfilePicture() == null ? null : mapStoredDocumentEntityToStoredDocumentResponseDTO(input.getProfilePicture()));
    }

    private StoredDocumentResponseDTO mapStoredDocumentEntityToStoredDocumentResponseDTO(StoredDocumentEntity input) {
        return new StoredDocumentResponseDTO(
                input.getId(),
                input.getName(),
                input.getFileName(),
                input.getExtension(),
                input.getStatus(),
                input.getDeleted(),
                input.getUrlFile()
        );
    }

    private DepartmentResponseDTO mapDepartmentEntityToDepartmentResponseDTO(DepartmentEntity input) {
        return new DepartmentResponseDTO(
                input.getId(),
                input.getName(),
                input.getUpperDepartment() == null ? null : input.getUpperDepartment().getId(),
                input.getChief().getId(),
                input.getCapacity(),
                input.getCreatedAt(),
                input.getUpdatedAt(),
                input.getIsActive()
        );
    }

    private DepartmentEntity mapDepartmentRequestDTOToDepartmentEntity(DepartmentRequestDTO input) {
        return new DepartmentEntity(
                null,
                input.name(),
                null,
                null,
                null,
                input.capacity(),
                null,
                null,
                null
        );
    }

        private UserResponseDTO mapUserEntityToUserResponseDTO(UserEntity input) {
        return new UserResponseDTO(
                input.getId(),
                input.getUsername(),
                input.getEmail(),
                input.getRole(),
                input.getEmployee().getId()
        );
    }

    private WorkstationEntity mapWorkstationRequestDTOToWorkstationEntity(WorkstationRequestDTO input) {
        return new WorkstationEntity(
                null,
                input.name(),
                mapHandbookRequestToHandbookEntity(input.handbook()),
                input.dependents(),
                null,
                null,
                null
        );
    }

    private HandbookEntity mapHandbookRequestToHandbookEntity(HandbookRequestDTO input) {
        return new HandbookEntity(
                null,
                input.objectives(),
                null,
                null,
                mapRequiredProfileRequestDTOToRequiredProfileEntity(input.requiredProfile()),
                mapSkillRequestDTOToSkillsEntity(input.skill())
        );
    }

    private RequiredProfileEntity mapRequiredProfileRequestDTOToRequiredProfileEntity(RequiredProfileRequestDTO input) {
        return new RequiredProfileEntity(
                null,
                input.ageCategory(),
                input.gender(),
                input.placeResidency(),
                input.timeAvailability(),
                input.contractType(),
                input.minimalEducation(),
                input.title(),
                input.experience()
        );
    }

    private SkillEntity mapSkillRequestDTOToSkillsEntity(SkillRequestDTO input) {
        return new SkillEntity(
                null,
                input.socialSkills(),
                input.manualSkills(),
                input.mentalSkills(),
                input.changeFlexibilities(),
                input.serviceVocation(),
                input.analyticalSkills()
        );
    }

    private WorkstationResponseDTO mapWorkstationEntityToWorkstationResponseDTO(WorkstationEntity input) {
        return new WorkstationResponseDTO(
                input.getId(),
                input.getName(),
                mapHandbookEntityToHandbookResponseDTO(input.getHandbook()),
                input.getDependents(),
                input.getIsActive()
        );
    }

    private HandbookResponseDTO mapHandbookEntityToHandbookResponseDTO(HandbookEntity input) {
        return new HandbookResponseDTO(
                input.getId(),
                input.getObjectives(),
                input.getResponsibilityId(),
                input.getFunctionId(),
                input.getRequiredProfile().getId(),
                input.getSkill().getId()
        );
    }

    private EmployeeDetailResponseDTO mapEmployeeEntityToEmployeeDetailResponseDTO(EmployeeEntity input) {
        return new EmployeeDetailResponseDTO(
                input.getId(),
                mapPersonEntityToPersonResponseDTO(input.getPerson()),
                input.getWorkstation().getId(),
                null,
                input.getStartDate(),
                input.getReasonForIncorporation(),
                input.getWorkload(),
                input.getWorkdayType(),
                input.getContractType(),
                input.getContractFrom(),
                input.getContractTo(),
                mapChiefRemarksEntityToChiefRemarksResponseDTO(input.getChiefRemarks()),
                mapRemunerationEntityToRemunerationResponseDTO(input.getRemuneration()),
                mapOtherRemarksEntityToOtherRemarksResponseDTO(input.getOtherRemarks()),
                mapAuthorizationEntityToAuthorizationResponseDTO(input.getAuthorization()),
                input.getReferent() == null ? null : input.getReferent().getId(),
                input.getIsReferent(),
                input.getIsActive()
        );
    }

    private AuthorizationResponseDTO mapAuthorizationEntityToAuthorizationResponseDTO(AuthorizationEntity input) {
        return new AuthorizationResponseDTO(
                input.getId(),
                input.getRequestReceived(),
                input.getComments(),
                input.getIsAuthorized()
        );
    }

    private OtherRemarksResponseDTO mapOtherRemarksEntityToOtherRemarksResponseDTO(OtherRemarksEntity input) {
        return new OtherRemarksResponseDTO(
                input.getId(),
                input.getManager(),
                input.getRequestReceived(),
                input.getComments()
        );
    }

    private RemunerationResponseDTO mapRemunerationEntityToRemunerationResponseDTO(RemunerationEntity input) {
        return new RemunerationResponseDTO(
                input.getId(),
                input.getMonthlySalary(),
                input.getAnnualSalary(),
                input.getComments()
        );
    }

    private ChiefRemarksResponseDTO mapChiefRemarksEntityToChiefRemarksResponseDTO(ChiefRemarksEntity input) {
        return new ChiefRemarksResponseDTO(
                input.getId(),
                input.getMembersTeamQuantity(),
                input.getEstimatedEffect()
        );
    }

    private WorkstationDetailResponseDTO mapWorkstationEntityToWorkstationDetailResponseDTO(WorkstationEntity input) {
        return new WorkstationDetailResponseDTO(
                input.getId(),
                input.getName(),
                mapHandbookEntityToHandbookDetailResponseDTO(input.getHandbook()),
                input.getDependents(),
                input.getIsActive()
        );
    }

    private HandbookDetailResponseDTO mapHandbookEntityToHandbookDetailResponseDTO(HandbookEntity input) {
        return new HandbookDetailResponseDTO(
                input.getId(),
                input.getObjectives(),
                null,
                null,
                mapRequiredProfileEntityToRequiredProfileResponseDTO(input.getRequiredProfile()),
                mapSkillEntityToSkillResponseDTO(input.getSkill())
        );
    }

    private RequiredProfileResponseDTO mapRequiredProfileEntityToRequiredProfileResponseDTO(RequiredProfileEntity inputRequiredProfile) {
        return new RequiredProfileResponseDTO(
                inputRequiredProfile.getId(),
                inputRequiredProfile.getAgeCategory(),
                inputRequiredProfile.getGender(),
                inputRequiredProfile.getPlaceResidency(),
                inputRequiredProfile.getTimeAvailability(),
                inputRequiredProfile.getContractType(),
                inputRequiredProfile.getMinimalEducation(),
                inputRequiredProfile.getTitle(),
                inputRequiredProfile.getExperience()
        );
    }

    private SkillResponseDTO mapSkillEntityToSkillResponseDTO(SkillEntity inputSkill) {
        return new SkillResponseDTO(
                inputSkill.getId(),
                inputSkill.getSocialSkills(),
                inputSkill.getManualSkills(),
                inputSkill.getMentalSkills(),
                inputSkill.getChangeFlexibilities(),
                inputSkill.getServiceVocation(),
                inputSkill.getAnalyticalSkills()
        );
    }
}
