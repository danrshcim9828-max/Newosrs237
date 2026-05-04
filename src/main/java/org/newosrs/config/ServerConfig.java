package org.newosrs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.newosrs.protocol.ProtocolProfile;

public record ServerConfig(
        String host,
        int port,
        int revision,
        int tickMillis,
        String cachePath,
        int handshakeOpcode,
        int loginOpcode) {
    public static ServerConfig defaults() {
        ProtocolProfile profile = ProtocolProfile.localRev237();
        return new ServerConfig("0.0.0.0", 43594, profile.revision(), 600, "./data/cache", profile.handshakeOpcode(), profile.loginInitOpcode());
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
