package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.retry.RetryFactory;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubWebClient(ScrapperAppConfig config) {
        return new GitHubClient(RetryFactory.createRetry(config.gitHubClientRetry()));
    }

    @Bean
    public StackOverflowClient stackOverflowWebClient(ScrapperAppConfig config) {
        return new StackOverflowClient(RetryFactory.createRetry(config.stackOverflowClientRetry()));
    }

    @Bean
    public BotClient botClient(ScrapperAppConfig config) {
        return new BotClient(RetryFactory.createRetry(config.botClientRetry()));
    }
}
