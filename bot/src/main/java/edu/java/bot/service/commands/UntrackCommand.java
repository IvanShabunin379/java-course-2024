package edu.java.bot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UntrackCommand implements Command {
    @Override
    public String name() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            String.valueOf(update.getMessage().getChat().getId()),
            "Данная команда ещё не реализована!"
        );

        // 1. Распознать ссылку;
        // 2. Вывести сообщение, если ссылки нет или ссылка некорректна;
        // 3. Удалить ссылку из списка отслеживаемых ссылок, если список содержит данную ссылку;
        // 4. Иначе (если в списке такой ссылки нет) вывести соотв. сообщение.
    }
}
