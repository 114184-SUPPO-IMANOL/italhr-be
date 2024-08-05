package org.apiitalhrbe.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "authorizations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate requestReceived;

    @Lob
    @Column
    private String comments;

    @Column(nullable = false)
    private Boolean isAuthorized;

}
