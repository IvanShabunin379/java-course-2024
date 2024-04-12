package edu.java.bot.service.commands;

import edu.java.bot.AbstractBotTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StartCommandTest extends AbstractBotTest {
    @Autowired
    StartCommand startCommand;

    @Test
    public void shouldHandleStartCommand() {
        // arrange
        Update update = mockUpdate();
        when(update.getMessage().getChat().getFirstName()).thenReturn("Иван");

        // act
        SendMessage sendMessage = startCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo("Здравствуйте, Иван! "
        + "Ваша регистрация прошла успешно и теперь Вы можете пользоваться данным ботом."
            + "\nЧтобы посмотреть список доступных команд, введите команду /help или откройте меню.");
    }
}
