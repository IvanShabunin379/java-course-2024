package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.AbstractClientTest;
import edu.java.client.BotClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class BotClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final BotClient botClient = new BotClient(server.baseUrl());

    @BeforeAll
    public static void beforeAll() {
        server.start();
    }

    @AfterAll
    public static void afterAll() {
        server.shutdown();
    }

    // Реализуйте тест для метода sendLinkUpdate класса BotClient
    // Тест должен быть реализован по аналогии с тестами из GitHubClientTest и StackOverflowClientTest
    @Test
    @SneakyThrows
    public void shouldSendLinkUpdate() {
    }
}
