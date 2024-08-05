package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private String socialSkills;

    @Lob
    @Column
    private String manualSkills;

    @Lob
    @Column
    private String mentalSkills;

    @Lob
    @Column
    private String changeFlexibilities;

    @Lob
    @Column
    private String serviceVocation;

    @Lob
    @Column
    private String analyticalSkills;

}
