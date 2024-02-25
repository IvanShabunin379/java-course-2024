package edu.java.bot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand implements Command {
    @SuppressWarnings("MultipleStringLiterals")
    private static final String HELP_MESSAGE =
        "Данный бот предоставляет единый способ отслеживания обновлений для множества интернет-ресурсов.\n"
            + "Вы можете вызвать необходимую команду с помощью меню, находящегося в левом нижнем углу"
            + ", или с помощью ввода команды с клавиатуры.\n\n"
            + "Список команд:\n\n"
            + new StartCommand().info() + ".\n\n"
            + new HelpCommand().info() + ".\n\n"
            + new TrackCommand().info() + ".\n"
            + "Важно! Имя ссылки для начала отслеживания необходимо указать через пробел после "
            + "/track" + ".\n\n"
            + new UntrackCommand().info() + ".\n"
            + "Важно! Имя ссылки для прекращения отслеживания необходимо указать через пробел после "
            + "/untrack" + ".\n\n"
            + new ListCommand().info() + ".";

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
        long chatId = update.getMessage().getChat().getId();
        return new SendMessage(String.valueOf(chatId), HELP_MESSAGE);
    }
}
