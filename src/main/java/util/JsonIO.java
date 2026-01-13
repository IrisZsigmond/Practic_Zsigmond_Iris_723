package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple JSON file helper.
 */
public final class JsonIO {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonIO() {}

    public static <T> T readOrCreate(Path path, String defaultJson, TypeReference<T> type) {
        try {
            if (Files.notExists(path)) {
                if (path.getParent() != null) Files.createDirectories(path.getParent());
                Files.writeString(path, defaultJson);
            }
            String json = Files.readString(path);
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + path, e);
        }
    }

    public static void writePretty(Path path, Object value) {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            String json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
            Files.writeString(path, json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON file: " + path, e);
        }
    }
}
