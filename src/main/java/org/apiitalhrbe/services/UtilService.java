package org.apiitalhrbe.services;

import org.apiitalhrbe.utils.enums.DocumentType;
import org.apiitalhrbe.utils.enums.Gender;
import org.apiitalhrbe.utils.enums.MaritalStatus;
import org.apiitalhrbe.utils.enums.Nationality;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilService {

    public List<String> getNationalities() {
        return Arrays.stream(Nationality.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<String> getDocumentTypes() {
        return Arrays.stream(DocumentType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<String> getMaritalStatus() {
        return Arrays.stream(MaritalStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<String> getGenders() {
        return Arrays.stream(Gender.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
