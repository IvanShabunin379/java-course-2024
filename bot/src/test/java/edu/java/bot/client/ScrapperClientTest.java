package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class ScrapperClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final ScrapperClient scrapperClient = new ScrapperClient(server.baseUrl());

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
    public void shouldRegisterChat() {
    }

    @Test
    @SneakyThrows
    public void shouldRemoveChat() {

    }

    @Test
    @SneakyThrows
    public void shouldGetLinks() {

    }

    @Test
    @SneakyThrows
    public void shouldTrackLink() {

    }

    @Test
    @SneakyThrows
    public void shouldUntrackLink() {

    }
}
