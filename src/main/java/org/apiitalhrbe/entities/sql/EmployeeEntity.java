package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private PersonEntity person;

    @ManyToOne
    @JoinColumn(name = "workstation_id")
    private WorkstationEntity workstation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private String reasonForIncorporation;

    @Column(nullable = false)
    private Integer workload;

    @Column(nullable = false)
    private String workdayType;

    @Column(nullable = false)
    private String contractType;

    @Column
    private LocalDate contractFrom;

    @Column
    private LocalDate contractTo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chief_remarks_id", referencedColumnName = "id")
    private ChiefRemarksEntity chiefRemarks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "remuneration_id", referencedColumnName = "id")
    private RemunerationEntity remuneration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "other_remarks_id", referencedColumnName = "id")
    private OtherRemarksEntity otherRemarks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authorization_id", referencedColumnName = "id")
    private AuthorizationEntity authorization;

    @ManyToOne
    @JoinColumn(name = "referent_id")
    private EmployeeEntity referent;

    @Column(nullable = false)
    private Boolean isReferent;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

}
