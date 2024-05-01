package edu.java.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Set;

import edu.java.retry.RetryBuilder;
import lombok.SneakyThrows;
import reactor.util.retry.Retry;

public abstract class AbstractClientTest {
    protected static final Retry DEFAULT_RETRY = new RetryBuilder()
            .withMaxAttempts(3)
            .withDuration(Duration.ofSeconds(2))
            .withStatusCodes(Set.of(500, 502, 503))
            .buildWithExponentialStrategy();

    @SneakyThrows
    public String jsonToString(String filePath) {
        return Files.readString(Paths.get(filePath));
    }
}
