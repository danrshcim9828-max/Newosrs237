package org.newosrs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record ServerConfig(String host, int port, int revision, int tickMillis, String cachePath) {
    public static ServerConfig defaults() {
        return new ServerConfig("0.0.0.0", 43594, 237, 600, "./data/cache");
    }

    public static ServerConfig load(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (!Files.exists(path)) {
            ServerConfig defaults = defaults();
            Files.createDirectories(path.getParent());
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), defaults);
            return defaults;
        }
        return mapper.readValue(path.toFile(), ServerConfig.class);
    }
}
