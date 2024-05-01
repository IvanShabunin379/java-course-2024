package edu.java.bot.service.commands;

import edu.java.bot.AbstractBotTest;
import edu.java.bot.client.ScrapperClient;
import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.ClientErrorException;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartCommandTest extends AbstractBotTest {
    // private static final Long UNREGISTERED_TG_CHAT_ID = 1L;
    private static final Long ALREADY_EXISTED_TG_CHAT_ID = 2L;
    private static final String START_MESSAGE = """
        Ваша регистрация прошла успешно, и теперь Вы можете пользоваться данным ботом.

        Чтобы посмотреть список доступных команд, введите команду /help или откройте меню.
        """;
    private static final String USER_ALREADY_REGISTERED_MESSAGE = """
        Вы уже зарегистрированы и можете пользоваться данным ботом.

        Чтобы посмотреть список доступных команд, введите команду /help или откройте меню.
        """;
    private static final String TEST_USERNAME = "Иван";

    StartCommand startCommand = new StartCommand(mockScrapperClient());

    protected ScrapperClient mockScrapperClient() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);

        ApiErrorResponse apiErrorResponse = mock(ApiErrorResponse.class);
        doThrow(new ClientErrorException(apiErrorResponse))
            .when(scrapperClient).registerChat(ALREADY_EXISTED_TG_CHAT_ID);

        return scrapperClient;
    }

    @Test
    public void shouldReturnHelloStartMessageWhenNewGoodTgChat() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChat().getFirstName()).thenReturn(TEST_USERNAME);

        // act
        SendMessage sendMessage = startCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo("Здравствуйте, " + TEST_USERNAME + "! " + START_MESSAGE);
    }

    @Test
    public void shouldReturnUserAlreadyRegisteredMessageWhenTgChat() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChatId()).thenReturn(ALREADY_EXISTED_TG_CHAT_ID);
        when(update.getMessage().getChat().getFirstName()).thenReturn(TEST_USERNAME);

        // act
        SendMessage sendMessage = startCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo(TEST_USERNAME + ", " + USER_ALREADY_REGISTERED_MESSAGE);
    }
}
