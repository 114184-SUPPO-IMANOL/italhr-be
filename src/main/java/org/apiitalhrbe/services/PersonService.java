package org.apiitalhrbe.services;

import jakarta.persistence.EntityNotFoundException;
import org.apiitalhrbe.dtos.request.PersonRequestDTO;
import org.apiitalhrbe.dtos.response.PersonResponseDTO;
import org.apiitalhrbe.entities.sql.PersonEntity;
import org.apiitalhrbe.repositories.sql.PersonRepository;
import org.apiitalhrbe.utils.*;
import org.apiitalhrbe.utils.enums.DocumentType;
import org.apiitalhrbe.utils.enums.Gender;
import org.apiitalhrbe.utils.enums.MaritalStatus;
import org.apiitalhrbe.utils.enums.Nationality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public PersonResponseDTO getPersonById(Long id) {
        PersonEntity personEntity = personRepository.getReferenceById(id);
        if (personEntity.getId() == null) {
            throw new EntityNotFoundException("Person not found");
        }
        return modelMapper.map(personEntity, PersonResponseDTO.class);
    }

    public PersonResponseDTO getPersonByDocumentNumber(String documentNumber) {
        Optional<PersonEntity> personEntity = personRepository.findByDocumentNumber(documentNumber);
        if (personEntity.isEmpty()) {
            throw new EntityNotFoundException("Person with document number " + documentNumber + " not found");
        }
        return modelMapper.map(personEntity.get(), PersonResponseDTO.class);
    }

    public boolean isValid(PersonRequestDTO person) {

        if (!isValidDocumentType(person.documentType())) {
            throw new IllegalArgumentException("Invalid document type");
        }

        if (!isValidGender(person.gender())) {
            throw new IllegalArgumentException("Invalid gender");
        }

        if (!isValidMaritalStatus(person.maritalStatus())) {
            throw new IllegalArgumentException("Invalid marital status");
        }

        if (!isValidNationality(person.nationality())) {
            throw new IllegalArgumentException("Invalid nationality");
        }

        if (person.birthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Person must be at least 18 years old");
        }
        return true;
    }

    private boolean isValidDocumentType(String documentType) {
        try {
            DocumentType.valueOf(documentType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidGender(String gender) {
        try {
            Gender.valueOf(gender);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidMaritalStatus(String maritalStatus) {
        try {
            MaritalStatus.valueOf(maritalStatus);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidNationality(String nationality) {
        try {
            Nationality.valueOf(nationality);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isExist(String documentNumber) {
        return personRepository.findByDocumentNumber(documentNumber).isPresent();
    }
}
