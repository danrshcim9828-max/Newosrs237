package org.newosrs.bootstrap;

import java.nio.file.Path;
import org.newosrs.cache.CacheIndex;
import org.newosrs.config.ServerConfig;
import org.newosrs.game.GameEngine;
import org.newosrs.net.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Bootstrap {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {
        ServerConfig config = ServerConfig.load(Path.of("config/server.json"));
        CacheIndex cacheIndex = new CacheIndex(Path.of(config.cachePath()));
        logger.info("Detected cache files: {}", cacheIndex.countArchiveFiles());

        GameEngine gameEngine = new GameEngine(config.tickMillis());
        GameServer gameServer = new GameServer(config);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gameServer.stop();
            gameEngine.stop();
        }));

        gameEngine.start();
        gameServer.start();
        logger.info("NewOSRS237 listening on {}:{} (rev {})", config.host(), config.port(), config.revision());
    }
}
