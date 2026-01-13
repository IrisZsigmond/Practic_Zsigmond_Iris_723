package service;

import model.Ereignis;
import repo.AbstractRepoImpl;
import repo.AbstractRepository;

import java.nio.file.Path;
import java.util.List;


public class EreignisService {
    Path path = Path.of("data/events.json");

    AbstractRepository<Ereignis, Integer> eventsRepo =
            new AbstractRepoImpl<>(
                    path,
                    Ereignis::getId,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Ereignis>>() {}
            );


}
