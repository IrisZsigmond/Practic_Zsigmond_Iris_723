package service;

import model.Fahrer;
import model.FahrerStatus;
import repo.AbstractRepoImpl;
import repo.AbstractRepository;

import java.nio.file.Path;
import java.util.List;

public class FahrerService {
    Path path = Path.of("data/drivers.json");

    AbstractRepository<Fahrer, Integer> fahrerRepo =
            new AbstractRepoImpl<>(
                    path,
                    Fahrer::getId,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Fahrer>>() {}
            );

    /**
     * ● Anzahl der Fahrer
     */
    public void printDriversCount() {
        long count = fahrerRepo.count();
        System.out.printf("Driverss loaded: %d%n", count);
    }


    /**
     *
     * die vollständige Liste der Fahrer in der Reihenfolge aus der Datei drivers.json (jeweils eine Zeile)
     * Ausgabeformat (Fahrer):
     * [#id] name (team) - status, skill=skillLevel
     *
     * [5] Charles Leclerc (Ferrari) - ACTIVE, skill=9
     * [12] Esteban Ocon (Alpine) - DNF, skill=7
     * [15] Valtteri Bottas (Sauber) - ACTIVE, skill=6
     */
    public List<String> printAllFahrers() {
        List<Fahrer> fahrers = fahrerRepo.findAll();
        return fahrers.stream()
                .map(fahrer -> String.format("[%d] %s (%s) - %s, skill=%d",
                        fahrer.getId(),
                        fahrer.getName(),
                        fahrer.getTeam(),
                        fahrer.getStatus(),
                        fahrer.getSkillLevel()))
                .toList();
    }

    /**
     * Filtern nach Team und Status
     * Lesen Sie von der Tastatur einen Teamnamen (String) ein.
     * Geben Sie anschließend nur die Fahrer aus, die gleichzeitig folgende Bedingungen erfüllen:
     * ● team == eingegebener Teamname
     * ● status == ACTIVE
     * Die Ausgabe erfolgt im gleichen Format wie in Aufgabe 1.
     * Ausgabe:
     * Input team: Red Bull
     * [1] Max Verstappen (Red Bull) - ACTIVE, skill=10
     * [2] Sergio Perez (Red Bull) - ACTIVE, skill=8
     */
    public List<String> filterActiveFahrersByTeam(String team) {
        List<Fahrer> fahrers = fahrerRepo.findAll();
        return fahrers.stream()
                .filter(f -> f.getTeam().equalsIgnoreCase(team) && f.getStatus() == FahrerStatus.ACTIVE)
                .map(fahrer -> String.format("[%d] %s (%s) - %s, skill=%d",
                        fahrer.getId(),
                        fahrer.getName(),
                        fahrer.getTeam(),
                        fahrer.getStatus(),
                        fahrer.getSkillLevel()))
                .toList();
    }
}
