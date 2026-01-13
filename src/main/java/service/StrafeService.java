package service;

import model.Strafe;
import repo.AbstractRepoImpl;
import repo.AbstractRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class StrafeService {
    Path path = Path.of("data/penalties.json");

    AbstractRepository<Strafe, Integer> strafeRepo =
            new AbstractRepoImpl<>(
                    path,
                    Strafe::getId,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Strafe>>() {}
            );

    /**
     * ● Anzahl der Strafen
     */
    public void printPenaltiesCount() {
        long count = strafeRepo.count();
        System.out.printf("Penalties loaded: %d%n", count);
    }

    /**
     * Get by FahrerId
     */
    public List<Strafe> getStrafebyFahrerId(Integer fahrerId) {
        List<Strafe> strafen = strafeRepo.findAll();
        return strafen.stream()
                .filter(strafe -> Objects.equals(strafe.getFahrerId(), fahrerId))
                .toList();
    }
    /**
     * For the given fahrerId, calculate the total values per it using the rules from getstrafenByFahrerId
     * return an Integer
     */
    public Integer calculateTotalGiftValueByFahrerId(Integer fahrerId) {
        List<Strafe> strafen = getStrafebyFahrerId(fahrerId);
        return strafen.stream()
                .mapToInt(Strafe::getSeconds)
                .sum();
    }
}