package edu.java.retry;

import java.time.Duration;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public record ClientRetryConfig(
    int maxAttempts,
    @NotNull Duration duration,
    @NotNull RetryDelayStrategy strategy,
    @NotNull Set<Integer> statusCodes
) {
}
