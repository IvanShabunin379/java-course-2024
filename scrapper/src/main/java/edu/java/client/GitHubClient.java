package edu.java.client;

import edu.java.responses.GitHubResponse;
import edu.java.utils.ApiErrorHandler;
import java.time.OffsetDateTime;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";

    private final WebClient webClient;
    private final Retry retry;

    public GitHubClient(Retry retry) {
        this.webClient = WebClient.create(BASE_URL);
        this.retry = retry;
    }

    public GitHubClient(@NotNull String url, Retry retry) {
        this.webClient = WebClient.create(url);
        this.retry = retry;
    }

    public List<GitHubResponse> getRepositoryUpdates(
        @NotNull String owner, @NotNull String repository,
        @NotNull OffsetDateTime fromTimestamp, @NotNull OffsetDateTime toTimestamp
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("repos/{owner}/{repo}/activity").build(owner, repository))
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .bodyToFlux(GitHubResponse.class)
            .filter(response ->
                response.timestamp().isAfter(fromTimestamp)
                    && (response.timestamp().isBefore(toTimestamp) || response.timestamp().isEqual(toTimestamp)))
            .switchIfEmpty(Flux.empty())
            .collectList()
            .retryWhen(retry)
            .block();
    }
}
