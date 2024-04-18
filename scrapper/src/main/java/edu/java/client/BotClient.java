package edu.java.client;

import edu.java.dto.LinkUpdateRequest;
import edu.java.utils.ApiErrorHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient {
    private static final String BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    public BotClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public BotClient(String url) {
        this.webClient = WebClient.create(url);
    }

    public void sendLinkUpdate(LinkUpdateRequest linkUpdateRequest) {
        webClient.post()
            .uri("/updates")
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleApiError)
            .toBodilessEntity()
            .block();
    }
}
