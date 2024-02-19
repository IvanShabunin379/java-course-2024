package edu.java.bot.service.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ListCommand implements Command {
    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(String.valueOf(update.getMessage().getChat().getId()), "...");
    }
}
