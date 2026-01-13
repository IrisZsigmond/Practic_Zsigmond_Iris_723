package service;

import model.Strafe;
import repo.AbstractRepoImpl;
import repo.AbstractRepository;

import java.nio.file.Path;
import java.util.List;

public class StrafeService {
    Path path = Path.of("data/penalties.json");

    AbstractRepository<Strafe, Integer> strafeRepo =
            new AbstractRepoImpl<>(
                    path,
                    Strafe::getId,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Strafe>>() {}
            );
}