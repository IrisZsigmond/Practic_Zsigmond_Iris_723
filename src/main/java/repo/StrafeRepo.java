package repo;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Strafe;

import java.nio.file.Path;

public class StrafeRepo extends AbstractRepoImpl<Strafe, Integer> {

    public StrafeRepo(Path filePath) {
        super(
                filePath,
                Strafe::getId,
                new TypeReference<java.util.List<Strafe>>() {}
        );
    }
}
