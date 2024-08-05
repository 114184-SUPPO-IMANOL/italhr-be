package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "workstation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkstationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "handbook_id", referencedColumnName = "id")
    private HandbookEntity handbook;

    @Column(nullable = false)
    private Integer dependents;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

    @Column(nullable = false)
    private Boolean isActive;
}
