package service;

import model.Fahrer;
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


}
