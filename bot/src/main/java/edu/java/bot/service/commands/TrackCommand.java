package edu.java.bot.service.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TrackCommand implements Command {
    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(String.valueOf(update.getMessage().getChat().getId()), "...");
    }

    @Override
    public boolean supports(Update update) {
        return false;
    }
}
