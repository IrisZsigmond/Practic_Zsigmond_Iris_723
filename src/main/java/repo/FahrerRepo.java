package repo;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Fahrer;

import java.nio.file.Path;

public class FahrerRepo extends AbstractRepoImpl<Fahrer, Integer> {

    public FahrerRepo(Path filePath) {
        super(
                filePath,
                Fahrer::getId,
                new TypeReference<java.util.List<Fahrer>>() {}
        );
    }
}
