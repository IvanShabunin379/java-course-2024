package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.retry.RetryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public ScrapperClient scrapperClient(BotAppConfig config) {
        return new ScrapperClient(RetryFactory.createRetry(config.scrapperClientRetry()));
    }
}
