package edu.java.client;

import edu.java.responses.GitHubResponse;
import java.time.OffsetDateTime;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public GitHubClient(String url) {
        this.webClient = WebClient.create(url);
    }

    public List<GitHubResponse> getRepositoryUpdate(
        @NotNull String owner, @NotNull String repository,
        @NotNull OffsetDateTime lastChecked
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("repos/{owner}/{repo}/activity")
                .queryParam("after", lastChecked.toString())
                .queryParam("direction", "asc")
                .build(owner, repository))
            .retrieve()
            .bodyToFlux(GitHubResponse.class)
            .switchIfEmpty(Flux.empty())
            .collectList()
            .block();

        //.onStatus(HttpStatus::is4xxClientError, clientResponse -> )
    }
}
