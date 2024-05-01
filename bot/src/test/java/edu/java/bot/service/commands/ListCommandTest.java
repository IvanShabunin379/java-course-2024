package edu.java.bot.service.commands;

import edu.java.bot.AbstractBotTest;
import edu.java.bot.client.ScrapperClient;
import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.exceptions.ClientResponseException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListCommandTest extends AbstractBotTest {
    private static final String EMPTY_LIST_MESSAGE = "Список отслеживаемых Вами ссылок пуст.";
    private static final String USER_NOT_REGISTERED_MESSAGE = """
        Извините, я не могу выполнить данную команду, так как Вы ещё не зарегистрированы.

        Для начала работы с ботом необходимо вызвать команду /start.
        """;
    private static final Long TG_CHAT_ID_WITH_TEST_TRACKED_LINKS = 1L;
    private static final Long TG_CHAT_ID_WITH_NO_TRACKED_LINKS = 2L;
    private static final Long UNREGISTERED_TG_CHAT_ID = 3L;
    private static final List<LinkResponse> TEST_TRACKED_LINKS = List.of(
        new LinkResponse(1L, URI.create("https://github.com/zinedin_zidane/java-course-2024")),
        new LinkResponse(2L, URI.create("https://stackoverflow.com/questions/123"))
    );

    ListCommand listCommand = new ListCommand(mockScrapperClient());

    protected ScrapperClient mockScrapperClient() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);

        when(scrapperClient.getLinks(TG_CHAT_ID_WITH_TEST_TRACKED_LINKS))
            .thenReturn(new ListLinksResponse(TEST_TRACKED_LINKS, (long) TEST_TRACKED_LINKS.size()));

        when(scrapperClient.getLinks(TG_CHAT_ID_WITH_NO_TRACKED_LINKS))
            .thenReturn(new ListLinksResponse(List.of(), 0L));

        ApiErrorResponse apiErrorResponse = mock(ApiErrorResponse.class);
        when(scrapperClient.getLinks(UNREGISTERED_TG_CHAT_ID))
            .thenThrow(new ClientResponseException(apiErrorResponse));

        return scrapperClient;
    }

    @Test
    public void shouldReturnTrackedLinksWhenTgChatHasTrackedLinks() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(TG_CHAT_ID_WITH_TEST_TRACKED_LINKS);

        // act
        SendMessage sendMessage = listCommand.handle(update);

        StringBuilder linksListMessage = new StringBuilder("Отслеживаемые Вами ссылки:\n");
        List<String> urls = TEST_TRACKED_LINKS.stream()
            .map(link -> link.url().toString())
            .toList();
        linksListMessage.append(String.join("\n\n", urls));

        // assert
        assertThat(sendMessage.getText()).isEqualTo(linksListMessage.toString());
    }

    @Test
    public void shouldReturnEmptyListMessageWhenTgChatHasNoTrackedLinks() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(TG_CHAT_ID_WITH_NO_TRACKED_LINKS);

        // act
        SendMessage sendMessage = listCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(EMPTY_LIST_MESSAGE);
    }

    @Test
    public void shouldReturnUserNotRegisteredMessageWhenTgChatNotFound() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(UNREGISTERED_TG_CHAT_ID);

        // act
        SendMessage sendMessage = listCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(USER_NOT_REGISTERED_MESSAGE);
    }
}
