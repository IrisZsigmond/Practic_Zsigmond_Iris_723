package service;

import model.Ereignis;
import repo.AbstractRepoImpl;
import repo.AbstractRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


public class EreignisService {
    Path path = Path.of("data/events.json");

    AbstractRepository<Ereignis, Integer> eventsRepo =
            new AbstractRepoImpl<>(
                    path,
                    Ereignis::getId,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Ereignis>>() {}
            );

    /**
     * ● Anzahl der Ereignisse
     */
    public void printEventsCount() {
        long count = eventsRepo.count();
        System.out.printf("Events loaded: %d%n", count);
    }

    /**
     * Punkteberechnung
     * Feste Regeln zur Berechnung der Punkte für verschiedene Arten von Ereignissen.
     * Implementieren Sie eine Methode, die für ein Ereignis den Wert computedPoints gemäß den folgenden Regeln berechnet:
     * ● OVERTAKE → basePoints + 1
     * ● FASTEST_LAP → basePoints + 2 * (lap % 3)
     * ● TRACK_LIMITS → basePoints - 5
     * ● COLLISION → basePoints - 10 - lap
     * ● PIT_STOP → basePoints + 2, falls lap <= 10, sonst basePoints
     * Geben Sie anschließend die ersten 5 Ereignisse aus der Datei events.json in folgender Form aus:
     * Event <id> -> raw=<basePoints> -> computed=<computedPoints>
     * Ausgabe:
     * Event 1 -> raw=10 -> computed=11
     */
    public List<String> calculateAndPrintEventPoints() {
        List<Ereignis> events = eventsRepo.findAll().stream().limit(5).toList();
        return events.stream().map(event -> {
            Integer computedPoints = switch (event.getTyp()) {
                case OVERTAKE -> event.getBasePoints() + 1;
                case FASTEST_LAP -> event.getBasePoints() + 2 * (event.getLap() % 3);
                case TRACK_LIMITS -> event.getBasePoints() - 5;
                case COLLISION -> event.getBasePoints() - 10 - event.getLap();
                case PIT_STOP -> event.getLap() <= 10 ? event.getBasePoints() + 2 : event.getBasePoints();
            };
            return String.format("Event %d -> raw=%d -> computed=%d",
                    event.getId(),
                    event.getBasePoints(),
                    computedPoints);
        }).toList();
    }

    /**
     * Get by tributId
     */
    public List<Ereignis> getEventsByFahrerId(Integer fahrerId) {
        return eventsRepo.findAll().stream()
                .filter(event -> Objects.equals(event.getFahrerId(), fahrerId))
                .toList();
    }

    /**
     * For the given events, calculate the total computed points per tributId using the rules from calculateAndPrintEventPoints and getEventsbyTributId.
     * return an Integer
     */
    public Integer getTotalComputedPointsByFahrerId(Integer fahrerId) {
        List<Ereignis> events = getEventsByFahrerId(fahrerId);
        return events.stream().mapToInt(event -> {
            return switch (event.getTyp()) {
                case OVERTAKE -> event.getBasePoints() + 1;
                case FASTEST_LAP -> event.getBasePoints() + 2 * (event.getLap() % 3);
                case TRACK_LIMITS -> event.getBasePoints() - 5;
                case COLLISION -> event.getBasePoints() - 10 - event.getLap();
                case PIT_STOP -> event.getLap() <= 10 ? event.getBasePoints() + 2 : event.getBasePoints();
            };
        }).sum();
    }

    /**
     * Abschlussbericht
     * Erstellen Sie die Datei data/race_report.txt.
     * Der Bericht soll eine Übersicht über die Ereignise enthalten.
     * Berechnen Sie auf Basis der Datei events.json die Anzahl der Ereignisse pro EreignisTyp.
     * und schreiben Sie das Ergebnis in die Datei. Die Ausgabe muss absteigend nach der Anzahl der Ereignisse sortiert sein.
     * OVERTAKE -> 4
     * FASTEST_LAP -> 3
     * PIT_STOP -> 3
     * TRACK_LIMITS -> 2
     * COLLISION -> 1
     */
    public void generateRaceReport() {
        List<Ereignis> events = eventsRepo.findAll();
        var eventTypeCount = events.stream()
                .collect(java.util.stream.Collectors.groupingBy(Ereignis::getTyp, java.util.stream.Collectors.counting()));

        List<String> reportLines = eventTypeCount.entrySet().stream()
                .map(entry -> String.format("%s -> %d", entry.getKey(), entry.getValue()))
                .toList();

        try {
            java.nio.file.Files.write(Path.of("data/race_report.txt"), reportLines);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
