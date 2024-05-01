package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.responses.StackOverflowResponse;
import edu.java.responses.StackOverflowResponse.StackOverflowAnswerInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class StackOverflowClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final StackOverflowClient stackOverflowClient = new StackOverflowClient(server.baseUrl());

    @BeforeAll
    public static void beforeAll() {
        server.start();
    }

    @AfterAll
    public static void afterAll() {
        server.shutdown();
    }

    @Test
    @SneakyThrows
    public void shouldGetGitHubResponses() {
        server.stubFor(get(urlPathMatching("/questions/\\d+/answers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonToString("src/test/resources/stackoverflow.json"))
                )
        );

        StackOverflowResponse response = stackOverflowClient.getQuestionUpdates(
                1L,
                OffsetDateTime.of(2024, 3, 7, 8, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.now()
        );
        List<StackOverflowAnswerInfo> items = response.items();

        assertThat(items).hasSize(2);
        assertThat(items.get(0).timestamp().toString()).isEqualTo("2024-03-08T07:36:24Z");
        assertThat(items.get(1).timestamp().toString()).isEqualTo("2024-03-07T11:06:40Z");
    }
}
