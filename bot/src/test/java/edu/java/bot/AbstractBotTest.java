package edu.java.bot;

import edu.java.bot.service.TelegramBot;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractBotTest {
    @MockBean
    TelegramBot bot;

    protected Update mockUpdate() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);

        when(chat.getId()).thenReturn(1L);
        when(message.getChat()).thenReturn(chat);
        when(update.getMessage()).thenReturn(message);

        return update;
    }
}
