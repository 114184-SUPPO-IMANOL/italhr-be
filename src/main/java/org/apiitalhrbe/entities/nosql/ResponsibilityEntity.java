package org.apiitalhrbe.entities.nosql;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "responsibility")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsibilityEntity {

    @Id
    private UUID id;

    private List<String> responsibilities;
}
