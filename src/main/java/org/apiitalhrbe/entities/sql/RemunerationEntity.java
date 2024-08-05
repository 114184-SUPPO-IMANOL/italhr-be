package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "remuneration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemunerationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer monthlySalary;

    @Column(nullable = false)
    private Integer annualSalary;

    @Lob
    @Column
    private String comments;

}
