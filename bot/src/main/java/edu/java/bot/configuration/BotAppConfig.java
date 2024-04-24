package edu.java.bot.configuration;

import edu.java.retry.ClientRetryConfig;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

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
