package repo;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Ereignis;

import java.nio.file.Path;

public class EreignisRepo extends AbstractRepoImpl<Ereignis, Integer> {

    public EreignisRepo(Path filePath) {
        super(
                filePath,
                Ereignis::getId,
                new TypeReference<java.util.List<Ereignis>>() {}
        );
    }
}
