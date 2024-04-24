package edu.java.bot.configuration;

import edu.java.retry.ClientRetryConfig;
import edu.java.retry.RetryDelayStrategy;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record BotAppConfig(
    @NotEmpty
    String telegramToken,
    @NotEmpty
    String botName,
    @NotNull
    ClientRetryConfig scrapperClientRetry
) {
}
