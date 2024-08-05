package org.apiitalhrbe.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartAndEndOfWeek {

    private LocalDate startOfWeek;

    private LocalDate endOfWeek;

}
