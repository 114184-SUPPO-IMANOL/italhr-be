package org.apiitalhrbe.entities.nosql;

import java.time.LocalDate;

public interface HistoryEntity {

    LocalDate getFrom();

    LocalDate getTo();
}
