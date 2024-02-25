package edu.java.configuration;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
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