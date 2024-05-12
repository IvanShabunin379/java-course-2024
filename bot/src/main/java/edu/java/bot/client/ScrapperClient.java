package edu.java.bot.client;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.utils.ApiErrorHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;
    private final Retry retry;

    public ScrapperClient(Retry retry) {
        this.webClient = WebClient.create(BASE_URL);
        this.retry = retry;
    }

    public ScrapperClient(String url, Retry retry) {
        this.webClient = WebClient.create(url);
        this.retry = retry;
    }

    public void registerChat(long chatId) {
        webClient.post()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .toBodilessEntity()
            .retryWhen(retry)
            .block();
    }

    public void removeChat(long chatId) {
        webClient.delete()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .toBodilessEntity()
            .retryWhen(retry)
            .block();
    }

    public ListLinksResponse getLinks(long chatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry)
            .block();
    }

    public LinkResponse trackLink(long chatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }

    public LinkResponse untrackLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ApiErrorHandler::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, ApiErrorHandler::handleServerError)
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }
}
