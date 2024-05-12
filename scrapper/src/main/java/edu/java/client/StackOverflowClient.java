package edu.java.client;

import edu.java.responses.StackOverflowResponse;
import edu.java.utils.ApiErrorHandler;
import java.time.OffsetDateTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com";

    private final WebClient webClient;
    private final Retry retry;

    public StackOverflowClient(Retry retry) {
        this.webClient = WebClient.create(BASE_URL);
        this.retry = retry;
    }

    public StackOverflowClient(@NotNull String url, Retry retry) {
        this.webClient = WebClient.create(url);
        this.retry = retry;
    }

    private Mono<Exception> handleError(ClientResponse response) {
        return Mono.empty();
    }

    public StackOverflowResponse getQuestionUpdates(
        long questionId,
        @NotNull OffsetDateTime fromTimestamp,
        @NotNull OffsetDateTime toTimestamp
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("questions/{ids}/answers")
                .queryParam("site", "stackoverflow")
                .queryParam("fromdate", fromTimestamp.toEpochSecond())
                .queryParam("todate", toTimestamp.toEpochSecond())
                .build(questionId))
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .bodyToMono(StackOverflowResponse.class)
            .retryWhen(retry)
            .block();
    }
}
