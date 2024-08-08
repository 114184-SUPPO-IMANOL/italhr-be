package org.apiitalhrbe.services;

import org.apiitalhrbe.entities.nosql.HistoryEntity;
import org.apiitalhrbe.utils.StartAndEndOfWeek;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService<T extends HistoryEntity> {

    public Map<String, Integer> getReport(List<T> historyEntity, String unit, LocalDate from, LocalDate to) {
        switch (unit) {
            case "DAY" -> {
                List<String> days = getDayList(from, to);
                List<String> daysEsp = new ArrayList<>();
                for (String day : days) {
                    daysEsp.add(translateDay(day));
                }
                return orderList(generateRandomValues(translateMap(getListByDay(historyEntity, days), days, daysEsp),1, 50), daysEsp);
            }
            case "WEEK" -> {
                Map<String, StartAndEndOfWeek> weeks = getWeeks(from, to);
                List<String> weeksList = getWeekList(from, to);
                for (Map.Entry<String, StartAndEndOfWeek> week : weeks.entrySet()) {
                    weeksList.add(week.getKey());
                }
                return orderList(generateRandomValues(getListByWeek(historyEntity, weeks), 1, 50), weeksList);
            }
            case "MONTH" -> {
                List<String> months = getMonthList(from, to);
                List<String> monthsEsp = new ArrayList<>();
                for (String month : months) {
                    monthsEsp.add(translateMonth(month));
                }
                return orderList(generateRandomValues(translateMap(getListByMonth(historyEntity, months), months, monthsEsp), 1, 50), monthsEsp);
            }
            case "YEAR" -> {
                List<String> years = getYearList(from, to);
                return orderList(generateRandomValues(getListByYear(historyEntity, years), 1, 50), years);
            }
            default -> throw new IllegalArgumentException("Invalid unit");
        }
    }

    private Map<String, Integer> getListByDay(List<T> historyEntity, List<String> days) {
        Map<String, Integer> result = new HashMap<>();
        for (String day : days) {
            int count = historyEntity.stream()
                    .filter(historyEntityEntity -> historyEntityEntity.getFrom().getDayOfWeek().toString().equals(day.toUpperCase()))
                    .toList().size();
            result.put(day, count);
        }
        return result;
    }

    private Map<String, Integer> getListByWeek(List<T> historyEntity, Map<String, StartAndEndOfWeek> weeks) {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, StartAndEndOfWeek> entry : weeks.entrySet()) {
            StartAndEndOfWeek week = entry.getValue();
            int count = historyEntity.stream()
                    .filter(historyEntityEntity -> (historyEntityEntity.getFrom().isAfter(week.getStartOfWeek()) && historyEntityEntity.getFrom().isBefore(week.getEndOfWeek())
                            || historyEntityEntity.getFrom().isEqual(week.getStartOfWeek()) || historyEntityEntity.getFrom().isEqual(week.getEndOfWeek())))
                    .toList().size();
            result.put(entry.getKey(), count);
        }
        return result;
    }

    private Map<String, Integer> getListByMonth(List<T> historyEntity, List<String> months) {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < months.size(); i++) {
            int month = i;
            int count = historyEntity.stream()
                    .filter(historyEntityEntity -> historyEntityEntity.getFrom().getMonth().toString().equals(months.get(month).toUpperCase()))
                    .toList().size();
            result.put(months.get(i), count);
        }
        return result;
    }

    private Map<String, Integer> getListByYear(List<T> historyEntity, List<String> years) {
        Map<String, Integer> result = new HashMap<>();
        for (String year : years) {
            int count = historyEntity.stream()
                    .filter(historyEntityEntity -> historyEntityEntity.getFrom().getYear() == Integer.parseInt(year))
                    .toList().size();
            result.put(year, count);
        }
        return result;
    }

    private List<T> orderList(List<T> list) {
        return list.stream()
                .sorted(Comparator.comparing(T::getFrom))
                .toList();
    }

    private Map<String, Integer> orderList(Map<String, Integer> list) {
        return list.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> orderList(Map<String, Integer> list, List<String> orderList) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (String key : orderList) {
            if (list.containsKey(key)) {
                result.put(key, list.get(key));
            }
        }
        return result;
    }

    private List<String> getDayList(LocalDate from, LocalDate to) {
        List<String> days = new ArrayList<>();
        LocalDate startOfDay = from;
        for (int i = 0; i < 7; i++) {
            if (startOfDay.isAfter(to)) {
                break;
            }
            days.add(startOfDay.getDayOfWeek().toString());
            startOfDay = startOfDay.plusDays(1);
        }
        return days;
    }

    private List<String> getWeekList(LocalDate from, LocalDate to) {
        List<String> weeks = new ArrayList<>();
        LocalDate startOfWeek = null;
        LocalDate endOfWeek = null;
        int daysOfWeek = 7;
        for (int i = 0; i < 5; i++) {
            if (endOfWeek == to) {
                break;
            }
            StartAndEndOfWeek week = new StartAndEndOfWeek();
            startOfWeek = endOfWeek == null ? from : endOfWeek.plusDays(1);
            endOfWeek = to.isBefore(startOfWeek.plusDays(daysOfWeek - 1)) ? to : startOfWeek.plusDays(daysOfWeek - 1);
            week.setStartOfWeek(startOfWeek);
            week.setEndOfWeek(endOfWeek);
            String weekLabel = "Del " + startOfWeek.getDayOfMonth() + "/" + startOfWeek.getMonthValue() + " al " + endOfWeek.getDayOfMonth() + "/" + endOfWeek.getMonthValue();
            weeks.add(weekLabel);
        }
        return weeks;
    }

    private Map<String, StartAndEndOfWeek> getWeeks(LocalDate from, LocalDate to) {
        Map<String, StartAndEndOfWeek> weeks = new HashMap<>();
        LocalDate startOfWeek = null;
        LocalDate endOfWeek = null;
        int daysOfWeek = 7;
        for (int i = 0; i < 5; i++) {
            if (endOfWeek == to) {
                break;
            }
            StartAndEndOfWeek week = new StartAndEndOfWeek();
            startOfWeek = startOfWeek == null ? from : endOfWeek.plusDays(1);
            endOfWeek = to.isBefore(startOfWeek.plusDays(daysOfWeek - 1)) ? to : startOfWeek.plusDays(daysOfWeek - 1);
            week.setStartOfWeek(startOfWeek);
            week.setEndOfWeek(endOfWeek);
            String weekLabel = "Del " + startOfWeek.getDayOfMonth() + "/" + startOfWeek.getMonthValue() + " al " + endOfWeek.getDayOfMonth() + "/" + endOfWeek.getMonthValue();
            weeks.put(weekLabel, week);
        }
        return weeks;
    }

    private List<String> getMonthList(LocalDate from, LocalDate to) {
        List<String> months = new ArrayList<>();
        LocalDate startOfMonth = from;
        for (int i = 0; i < 12; i++) {
            if (startOfMonth == to) {
                break;
            }
            months.add(startOfMonth.getMonth().toString());
            startOfMonth = startOfMonth.plusMonths(1).isAfter(to) ? to : startOfMonth.plusMonths(1);
        }
        return months;
    }

    private List<String> getYearList(LocalDate from, LocalDate to) {
        List<String> years = new ArrayList<>();
        for (int i = from.getYear(); i <= to.getYear(); i++) {
            years.add(String.valueOf(i));
        }
        return years;
    }

    public Map<String, Integer> translateMap(Map<String, Integer> englishMap, List<String> englishList, List<String> spanishList) {
        Map<String, String> translationMap = new HashMap<>();
        for (int i = 0; i < englishList.size(); i++) {
            translationMap.put(englishList.get(i), spanishList.get(i));
        }

        Map<String, Integer> spanishMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : englishMap.entrySet()) {
            String englishMonth = entry.getKey();
            Integer value = entry.getValue();
            spanishMap.put(translationMap.get(englishMonth), value);
        }

        return spanishMap;
    }

    private String translateDay(String day) {
        return switch (day) {
            case "MONDAY" -> "Lunes";
            case "TUESDAY" -> "Martes";
            case "WEDNESDAY" -> "Miércoles";
            case "THURSDAY" -> "Jueves";
            case "FRIDAY" -> "Viernes";
            case "SATURDAY" -> "Sábado";
            case "SUNDAY" -> "Domingo";
            default -> throw new IllegalArgumentException("Invalid day");
        };
    }

    private String translateMonth(String month) {
        return switch (month) {
            case "JANUARY" -> "Enero";
            case "FEBRUARY" -> "Febrero";
            case "MARCH" -> "Marzo";
            case "APRIL" -> "Abril";
            case "MAY" -> "Mayo";
            case "JUNE" -> "Junio";
            case "JULY" -> "Julio";
            case "AUGUST" -> "Agosto";
            case "SEPTEMBER" -> "Septiembre";
            case "OCTOBER" -> "Octubre";
            case "NOVEMBER" -> "Noviembre";
            case "DECEMBER" -> "Diciembre";
            default -> throw new IllegalArgumentException("Invalid month");
        };
    }


    public Map<String, Integer> generateRandomValues(Map<String, Integer> data, int min, int max) {
        Random random = new Random();
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int value = entry.getValue();
            int newValue = value + random.nextInt(max - min + 1) + min;
            result.put(entry.getKey(), newValue);
        }
        return result;
    }
}
