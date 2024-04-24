package edu.java.configuration;

import edu.java.retry.ClientRetryConfig;
import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperAppConfig(
    @NotNull Scheduler scheduler,
    @NotNull ClientRetryConfig gitHubClientRetry,
    @NotNull ClientRetryConfig stackOverflowClientRetry,
    @NotNull ClientRetryConfig botClientRetry,
    @NotNull AccessType databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
