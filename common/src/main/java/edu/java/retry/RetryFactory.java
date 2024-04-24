package edu.java.retry;

import lombok.experimental.UtilityClass;
import reactor.util.retry.Retry;

@UtilityClass
public class RetryFactory {
    public static Retry createRetry(@NotNull ClientRetryConfig retryConfig) {
        RetryBuilder retryBuilder = new RetryBuilder()
            .withMaxAttempts(retryConfig.maxAttempts())
            .withDuration(retryConfig.duration())
            .withStatusCodes(retryConfig.statusCodes());

        return switch (retryConfig.strategy()) {
            case RetryDelayStrategy.CONSTANT -> retryBuilder.buildWithConstantStrategy();
            case RetryDelayStrategy.LINEAR -> retryBuilder.buildWithLinealStrategy();
            case RetryDelayStrategy.EXPONENTIAL -> retryBuilder.buildWithExponentialStrategy();
        };
    }
}
