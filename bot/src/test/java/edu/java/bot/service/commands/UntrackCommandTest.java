package edu.java.bot.service.commands;

import edu.java.bot.AbstractBotTest;
import edu.java.bot.client.ScrapperClient;
import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.exceptions.ClientResponseException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UntrackCommandTest extends AbstractBotTest {
    private static final Long GOOD_CHAT_ID = 1L;
    private static final Long UNREGISTERED_CHAT_ID = 2L;
    private static final Long ALREADY_UNTRACKED_LINK_CHAT_ID = 3L;
    private static final URI TEST_LINK_URL = URI.create("https://stackoverflow.com/questions/123");

    private static final String UNKNOWN_LINK_TYPE_MESSAGE = """
        Бот не может распознать ссылку, которую Вы попытались прекратить отслеживать.

        На данный момент отслеживание поддерживается только для вопросов со StackOverflow и репозиториев GitHub.
        Вводите ссылку, которую Вы хотите прекратить отслеживать, строго через один пробел после команды /untrack.
        """;
    private static final String USER_NOT_REGISTERED_MESSAGE = """
        Извините, я не могу выполнить данную команду, так как Вы ещё не зарегистрированы.

        Для начала работы с ботом необходимо вызвать команду /start.
        """;
    private static final String LINK_ALREADY_UNTRACKED_MESSAGE = "Данная ссылка отсутствует в списке отслеживаемых.";
    private static final String ONLY_COMMAND_NAME_MESSAGE =
        "Для успешного выполнения команды /untrack, вводите ссылку, которую Вы хотите прекратить отслеживать, "
            + "строго через один пробел после /untrack!";

    UntrackCommand untrackCommand = new UntrackCommand(mockScrapperClient());

    protected ScrapperClient mockScrapperClient() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);

        when(scrapperClient.untrackLink(GOOD_CHAT_ID, new RemoveLinkRequest(TEST_LINK_URL)))
            .thenReturn(new LinkResponse(1L, TEST_LINK_URL));

        ApiErrorResponse tgChatNotFoundApiErrorResponse = mock(ApiErrorResponse.class);
        when(tgChatNotFoundApiErrorResponse.exceptionName()).thenReturn("TgChatNotFoundException");
        when(scrapperClient.untrackLink(UNREGISTERED_CHAT_ID, new RemoveLinkRequest(TEST_LINK_URL)))
            .thenThrow(new ClientResponseException(tgChatNotFoundApiErrorResponse));

        ApiErrorResponse linkInChatNotFoundApiErrorResponse = mock(ApiErrorResponse.class);
        when(linkInChatNotFoundApiErrorResponse.exceptionName()).thenReturn("LinkInChatNotFoundException");
        when(scrapperClient.untrackLink(ALREADY_UNTRACKED_LINK_CHAT_ID, new RemoveLinkRequest(TEST_LINK_URL)))
            .thenThrow(new ClientResponseException(linkInChatNotFoundApiErrorResponse));

        return scrapperClient;
    }

    @Test
    public void shouldReturnSuccessMessageWhenLinkAndChatAreGood() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(GOOD_CHAT_ID);
        when(update.getMessage().getText()).thenReturn("/track " + TEST_LINK_URL);

        // act
        SendMessage sendMessage = untrackCommand.handle(update);

        // assert
        assertThat(sendMessage.getText())
            .isEqualTo("Ссылка " + TEST_LINK_URL + " больше не отслеживается.");
    }

    @Test
    public void shouldReturnUserNotRegisteredMessageWhenTgChatNotFound() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(UNREGISTERED_CHAT_ID);
        when(update.getMessage().getText()).thenReturn("/untrack " + TEST_LINK_URL);

        // act
        SendMessage sendMessage = untrackCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(USER_NOT_REGISTERED_MESSAGE);
    }

    @Test
    public void shouldReturnLinkAlreadyUntrackedMessageWhenLinkInChatNotFound() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(ALREADY_UNTRACKED_LINK_CHAT_ID);
        when(update.getMessage().getText()).thenReturn("/untrack " + TEST_LINK_URL);

        // act
        SendMessage sendMessage = untrackCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(LINK_ALREADY_UNTRACKED_MESSAGE);
    }

    @Test
    public void shouldReturnUnknownLinkTypeMessageWhenUnknownLinkType() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(GOOD_CHAT_ID);
        when(update.getMessage().getText()).thenReturn("/track " + "aaa.com");

        // act
        SendMessage sendMessage = untrackCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(UNKNOWN_LINK_TYPE_MESSAGE);
    }

    @Test
    public void shouldReturnOnlyCommandNameMessageWhenThereIsNoLinkForUntracking() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(GOOD_CHAT_ID);
        when(update.getMessage().getText()).thenReturn("/untrack");

        // act
        SendMessage sendMessage = untrackCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(ONLY_COMMAND_NAME_MESSAGE);
    }
}
