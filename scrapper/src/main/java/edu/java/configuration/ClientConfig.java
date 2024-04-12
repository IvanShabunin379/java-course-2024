package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubWebClient() {
        return new GitHubClient();
    }

    @Bean
    public StackOverflowClient stackOverflowWebClient() {
        return new StackOverflowClient();
    }
}
