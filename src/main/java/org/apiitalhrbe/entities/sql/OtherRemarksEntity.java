package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "other_remarks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherRemarksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String manager;

    @Column()
    private LocalDate requestReceived;

    @Lob
    @Column
    private String comments;

}
