package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chief_remarks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiefRemarksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer membersTeamQuantity;

    @Column
    private String estimatedEffect;

}
