package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "required_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequiredProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer ageCategory;

    @Column
    private String gender;

    @Column
    private String placeResidency;

    @Column
    private String timeAvailability;

    @Column
    private String contractType;

    @Column
    private String minimalEducation;

    @Column
    private String title;

    @Column
    private String experience;
}
