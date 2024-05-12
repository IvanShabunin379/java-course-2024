package edu.java.client;

import edu.java.dto.LinkUpdateRequest;
import edu.java.service.link_updates_sender.LinkUpdatesSender;
import edu.java.utils.ApiErrorHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class BotClient implements LinkUpdatesSender {
    private static final String BASE_URL = "http://localhost:8090";

    private final WebClient webClient;
    private final Retry retry;

    public BotClient(Retry retry) {
        this.webClient = WebClient.create(BASE_URL);
        this.retry = retry;
    }

    public BotClient(String url, Retry retry) {
        this.webClient = WebClient.create(url);
        this.retry = retry;
    }

    public void sendLinkUpdate(LinkUpdateRequest linkUpdateRequest) {
        webClient.post()
            .uri("/updates")
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .toBodilessEntity()
            .retryWhen(retry)
            .block();
    }
}
