package org.newosrs.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class CacheIndex {
    private final Path root;

    public CacheIndex(Path root) {
        this.root = root;
    }

    public long countArchiveFiles() throws IOException {
        if (!Files.exists(root)) {
            return 0;
        }
        try (Stream<Path> files = Files.walk(root)) {
            return files.filter(Files::isRegularFile).count();
        }
    }
}
