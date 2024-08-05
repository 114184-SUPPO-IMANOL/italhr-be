package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity upperDepartment;

    @ManyToOne
    @JoinColumn(name = "chief_id")
    private EmployeeEntity chief;

    @OneToMany
    @JoinColumn(name = "department_id")
    private List<EmployeeEntity> employees;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

    @Column(nullable = false)
    private Boolean isActive;
}
