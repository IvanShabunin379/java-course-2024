package edu.java.bot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
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
        return new SendMessage(
            String.valueOf(update.getMessage().getChat().getId()),
            "Данная команда ещё не реализована!"
        );

        // 1. Распознать ссылку;
        // 2. Вывести сообщение, если ссылки нет или ссылка некорректна;
        // 3. Добавить ссылку в список отслеживаемых ссылок, если список еще не содержит данной ссылки;
        // 4. Иначе (если в списке уже содержится данная ссылка) вывести соотв. сообщение.
    }
}
