package edu.java.bot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand implements Command {
    @SuppressWarnings("LineLength")
    private static final String HELP_MESSAGE = """
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
        """;

    @Override
    public String name() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        return new SendMessage(String.valueOf(chatId), HELP_MESSAGE);
    }
}
