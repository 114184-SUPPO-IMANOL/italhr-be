package org.apiitalhrbe.entities.nosql;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Document(collection = "hardcoded_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HardcodedHistoryEntity implements HistoryEntity {

    @Id
    private UUID id;

    private String status;

    private String type;

    private LocalDate from;

    private LocalDate to;

    private Map<String, Integer> hardcodedValues;
}
