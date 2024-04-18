package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.AbstractClientTest;
import edu.java.client.GitHubClient;
import edu.java.responses.GitHubResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class GitHubClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final GitHubClient githubClient = new GitHubClient(server.baseUrl());

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
        server.stubFor(get(urlPathMatching("/repos/owner/repository/activity"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github.json"))
            )
        );

        List<GitHubResponse> responses = githubClient.getRepositoryUpdates(
            "owner",
            "repository",
            OffsetDateTime.of(2024, 3, 7, 20, 5, 0, 0, ZoneOffset.UTC)
        );

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).activityType()).isEqualTo("branch_creation");
        assertThat(responses.get(0).timestamp().toString()).isEqualTo("2024-03-07T21:11:39Z");
        assertThat(responses.get(1).activityType()).isEqualTo("push");
        assertThat(responses.get(1).timestamp().toString()).isEqualTo("2024-03-07T20:09:03Z");
    }
}
