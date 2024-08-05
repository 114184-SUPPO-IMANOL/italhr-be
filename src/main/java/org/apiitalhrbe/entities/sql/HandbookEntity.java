package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "handbook")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandbookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String objectives;

    @Column(nullable = false)
    private UUID responsibilityId;

    @Column(nullable = false)
    private UUID functionId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "required_profile_id", referencedColumnName = "id")
    private RequiredProfileEntity requiredProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private SkillEntity skill;

}
