package edu.java.retry;

import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import java.util.Set;

public record ClientRetryConfig(
    int maxAttempts,
    @NotNull Duration duration,
    @NotNull RetryDelayStrategy strategy,
    @NotNull Set<Integer> statusCodes
) {
}
