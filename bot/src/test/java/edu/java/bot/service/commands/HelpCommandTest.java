package edu.java.bot.service.commands;

import edu.java.bot.AbstractBotTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;

public class HelpCommandTest extends AbstractBotTest {
    @Autowired
    HelpCommand helpCommand;

    @SuppressWarnings("LineLength")
    @Test
    public void shouldHandleHelpCommand() {
        // arrange
        Update update = mockUpdate();

        // act
        SendMessage sendMessage = helpCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo("""
            Данный бот предоставляет единый способ отслеживания обновлений для множества интернет-ресурсов.
            На данный момент отслеживание поддерживается только для вопросов со StackOverflow и репозиториев GitHub.

            Вы можете вызвать необходимую команду с помощью меню, находящегося в левом нижнем углу, или с помощью ввода команды с клавиатуры.

            Список команд:
            /start - начать работу с данным ботом.

            /help - вывести окно с командами.

            /track - начать отслеживание ссылки.
            Важно! Имя ссылки для начала отслеживания необходимо указать через пробел после /track.

            /untrack - прекратить отслеживание ссылки.
            Важно! Имя ссылки для прекращения отслеживания необходимо указать через пробел после /untrack.

            /list - показать список отслеживаемых ссылок.
            """);
    }
}
