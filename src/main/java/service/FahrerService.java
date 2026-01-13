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
}
