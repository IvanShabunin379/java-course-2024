package edu.java.bot.client;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    private static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    public ScrapperClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public ScrapperClient(String url) {
        this.webClient = WebClient.create(url);
    }

    public void registerChat(long chatId) {
        webClient.post()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    public void removeChat(long chatId) {
        webClient.delete()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    public ListLinksResponse getLinks(long chatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse trackLink(long chatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    /* public LinkResponse untrackLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.delete()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    } */
}
