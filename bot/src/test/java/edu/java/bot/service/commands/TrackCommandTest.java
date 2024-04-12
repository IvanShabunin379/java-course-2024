package edu.java.bot.service.commands;

import edu.java.bot.AbstractBotTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;

public class TrackCommandTest extends AbstractBotTest {
    @Autowired
    TrackCommand trackCommand;

    @Test
    public void shouldHandleTrackCommand() {
        // arrange
        Update update = mockUpdate();

        // act
        SendMessage sendMessage = trackCommand.handle(update);

        // assert
        assertThat(sendMessage.getText()).isEqualTo("Данная команда ещё не реализована!");
    }
}
