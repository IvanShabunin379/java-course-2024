package edu.java.bot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ListCommand implements Command {
    private static final String EMPTY_LIST_MESSAGE = "Список отслеживаемых сслылок пуст.";

    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    // Добавить вывод списка ссылок, если список не пуст.
    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(String.valueOf(update.getMessage().getChat().getId()), EMPTY_LIST_MESSAGE);
    }
}
