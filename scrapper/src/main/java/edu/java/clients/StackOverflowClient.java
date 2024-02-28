package edu.java.clients;

import edu.java.responses.StackOverflowResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public StackOverflowClient(String url) {
        this.webClient = WebClient.create(url);
    }

    public StackOverflowResponse getQuestionUpdate(long questionId) {
        return null;
    }
}
