package edu.java.clients;

import edu.java.responses.StackOverflowResponse;
import java.time.OffsetDateTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public StackOverflowClient(@NotNull String url) {
        this.webClient = WebClient.create(url);
    }

    private Mono<Exception> handleError(ClientResponse response) {
        return Mono.empty();
    }

    public StackOverflowResponse getQuestionUpdates(long questionId, @NotNull OffsetDateTime fromTimestamp) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("questions/{ids}/answers")
                .queryParam("site", "stackoverflow")
                .queryParam("fromdate", fromTimestamp.toEpochSecond())
                .build(questionId))
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }
}
