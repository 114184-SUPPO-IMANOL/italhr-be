package org.apiitalhrbe.entities.nosql;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "workstation_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkstationHistoryEntity implements HistoryEntity {

    @Id
    private UUID id;

    private Long workstationId;

    private String state;

    private LocalDate from;

    private LocalDate to;
}
