package edu.java.clients;

import edu.java.responses.GitHubResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public GitHubClient(String url) {
        this.webClient = WebClient.create(url);
    }

    public GitHubResponse getRepositoryUpdate(@NotNull String owner, @NotNull String repository) {
        webClient.get()
            .uri("repos/{owner}/{repo}", owner, repository)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
        //.onStatus(HttpStatus::is4xxClientError, clientResponse -> )

        return null;
    }
}
