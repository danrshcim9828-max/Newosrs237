package org.newosrs.game;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameEngine {
    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final int tickMillis;
    private final AtomicLong tickCounter = new AtomicLong();
    private Instant startedAt;

    public GameEngine(int tickMillis) {
        this.tickMillis = tickMillis;
    }

    public void start() {
        startedAt = Instant.now();
        executor.scheduleAtFixedRate(() -> {
            long tick = tickCounter.incrementAndGet();
            if (tick % 100 == 0) {
                long seconds = Duration.between(startedAt, Instant.now()).toSeconds();
                logger.info("Game loop healthy at tick={} uptime={}s", tick, seconds);
            }
        }, tickMillis, tickMillis, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }
}
