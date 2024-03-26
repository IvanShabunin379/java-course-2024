package edu.java.client;

import edu.java.responses.StackOverflowResponse;
import io.netty.channel.ChannelOption;
import java.time.OffsetDateTime;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com";
    private static final int MILLIS_BEFORE_CONNECT = 10000;
    private final WebClient webClient;

    public StackOverflowClient() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, MILLIS_BEFORE_CONNECT);
        this.webClient = WebClient.builder()
            .defaultStatusHandler(HttpStatusCode::isError, this::handleError)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl(BASE_URL)
            .build();
    }

    public StackOverflowClient(String url) {
        this.webClient = WebClient.create(url);
    }

    private Mono<Exception> handleError(ClientResponse response) {
        return Mono.empty();
    }

    public List<StackOverflowResponse> getQuestionUpdate(long questionId, @NotNull OffsetDateTime lastChecked) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("questions/{ids}/answers")
                .queryParam("site", "stackoverflow")
                .queryParam("fromdate", lastChecked.toEpochSecond())
                .queryParam("sort", "creation")
                .queryParam("order", "desc")
                .build(questionId))
            .retrieve()
            .bodyToFlux(StackOverflowResponse.class)
            .switchIfEmpty(Flux.empty())
            .collectList()
            .block();
    }
}
