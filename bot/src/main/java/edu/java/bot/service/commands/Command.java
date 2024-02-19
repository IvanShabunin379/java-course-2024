package edu.java.bot.service.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public interface Command {
    String name();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        return update.getMessage().getText().equals(name());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(name(), description());
    }
}
